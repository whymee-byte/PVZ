from pathlib import Path
import subprocess,random,re,json,hashlib

ROOT=Path(__file__).resolve().parent.parent
OUT=ROOT/'review/deep'
JDK=Path('C:/Program Files/Java/jdk-25.0.4/bin')
OUT.mkdir(exist_ok=True)
CLASSES=OUT/'classes'
def run(args,data=None):
    return subprocess.run(args,input=data,capture_output=True,text=True,encoding='utf-8',timeout=45,cwd=ROOT)
sources=list((ROOT/'PlantillaPR1CV/src').rglob('*.java'))
c=run([str(JDK/'javac.exe'),'-encoding','UTF-8','-Xlint:all','-d',str(CLASSES)]+[str(p) for p in sources]+[str(ROOT/'review/DeepChecks.java')])
(OUT/'compile.txt').write_text(c.stdout+c.stderr,encoding='utf-8')
if c.returncode:raise SystemExit(c.stderr)
r=run([str(JDK/'java.exe'),'-Dstdout.encoding=UTF-8','-cp',str(CLASSES),'pvz.logic.DeepChecks'])
(OUT/'results.txt').write_text(r.stdout+r.stderr,encoding='utf-8')
print(r.stdout)
base=[str(JDK/'java.exe'),'-Dstdout.encoding=UTF-8','-Dstderr.encoding=UTF-8','-cp',str(CLASSES),'pvz.PlantsVsZombies']
results=[]
def cli(name,args,data,check):
    r=run(base+args,data)
    try:
        assert r.returncode==0, r.stderr
        assert not r.stderr,r.stderr
        check(r.stdout)
        status='PASS';error=''
    except AssertionError as e:
        status='FAIL';error=str(e)
        (OUT/(name+'-input.txt')).write_text(data,encoding='utf-8')
        (OUT/(name+'-output.txt')).write_text(r.stdout+r.stderr,encoding='utf-8')
    results.append(dict(name=name,status=status,error=error,returncode=r.returncode))
    return r.stdout
def contains(*parts):
    def check(t):
        for p in parts:assert p in t,p
    return check
for name,args,needle in [('no-args',[],'Usage:'),('bad-level',['bad'],'Level must be'),('bad-seed',['easy','abc'],'seed'),('overflow-seed',['easy','9223372036854775808'],'seed'),('extra-args',['easy','25','x'],'Usage:')]:
    cli(name,args,'',contains(needle))
for ending in ['e','exit','EXIT','   ExIt   ']:
    cli('exit-'+str(len(results)),['easy','25'],ending,contains('Game over','Player leaves the game'))
cli('whitespace',['easy','25'],'   \n\t\n\tA\tS\t0\t0\nexit\n',contains('Number of cycles: 3','Sun coins: 30','Player leaves the game'))
cli('invalid-then-valid',['easy','25'],'add p x 0\ncatch 0 abc\nreset hard nope\nexit extra\nhelp\nexit\n',contains('[ERROR]','Player leaves the game'))
cli('reset-new-level',['easy','25'],'reset insane 123\nnone\nreset\nexit\n',contains('Remaining zombies: 10','Number of cycles: 0','Player leaves the game'))
for lv in ['easy','hard','insane']:
    for seed in range(12):
        rng=random.Random(9123+seed)
        lines=[]
        for i in range(180):
            r,c=rng.randrange(4),rng.randrange(8)
            lines.append(rng.choice(['none','',f'cp {rng.choice(["s","p","wn","c"])} {c} {r}',f'az {rng.choice(["z","bz","sz","ez"])} {c} {r}',f'c {c} {r}',f'a p {c} {r}','help','list','lz','a bad -1 5','reset']))
        lines.append('exit')
        data='\n'.join(lines)+'\n'
        def check(t):
            assert 'Game over' in t,'no final'
            for row in t.splitlines():
                if row.lstrip().startswith('|'):
                    cells=row.split('|')[1:-1]
                    assert len(cells)==8,'wrong board width'
                    for cell in cells:
                        assert len(cell)==12,'cell width'
                        assert cell.count('*')<=1,'duplicate suns'
                        assert '[00]' not in cell and '[-' not in cell,'dead displayed'
            assert not re.search(r'Sun coins: -|Remaining zombies: -|Caught suns: -',t),'negative counter'
        t=cli('random-'+lv+'-'+str(seed),[lv,str(seed)],data,check)
        t2=cli('replay-'+lv+'-'+str(seed),[lv,str(seed)],data,check)
        results.append(dict(name='deterministic-'+lv+'-'+str(seed),status='PASS' if t==t2 else 'FAIL',error='' if t==t2 else 'same seed/input differs'))
scan=[]
for p in sources:
    text=p.read_text(encoding='utf-8')
    for i,l in enumerate(text.splitlines(),1):
        if re.search(r'\binstanceof\b|\.getClass\s*\(|\.isInstance\s*\(|\bis(?:Zombie|Plant|Sun|Peashooter)\s*\(',l):scan.append(dict(file=str(p),line=i,text=l))
summary=dict(cli_total=len(results),cli_pass=sum(x['status']=='PASS' for x in results),cli_fail=sum(x['status']=='FAIL' for x in results),cases=results,type_identification_matches=scan,source_sha256={str(p.relative_to(ROOT)):hashlib.sha256(p.read_bytes()).hexdigest() for p in sources})
(OUT/'cli-summary.json').write_text(json.dumps(summary,ensure_ascii=False,indent=2),encoding='utf-8')
print('CLI TOTAL',summary['cli_total'],'PASS',summary['cli_pass'],'FAIL',summary['cli_fail'])
print('TYPE IDENTIFICATION MATCHES',scan)
for x in results:
    if x['status']=='FAIL':print(x)
