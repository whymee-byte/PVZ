from pathlib import Path
import subprocess, re, difflib, json

ROOT=Path(__file__).resolve().parents[2]
OUT=ROOT/'review/p2'
TESTS=ROOT/'PlantillaPR1CV/p2'
JDK=Path('C:/Program Files/Java/jdk-25.0.4/bin')
CLASSES=OUT/'classes'
CLASSES.mkdir(exist_ok=True)
def run(args, data=None):
    return subprocess.run(args,input=data,capture_output=True,text=True,encoding='utf-8',timeout=30,cwd=ROOT)
c=run([str(JDK/'javac.exe'),'-encoding','UTF-8','-d',str(CLASSES)]+[str(p) for p in (ROOT/'PlantillaPR1CV/src').rglob('*.java')])
(OUT/'compile.txt').write_text(c.stdout+c.stderr,encoding='utf-8')
if c.returncode: raise SystemExit(c.stderr)
summary=[]
for p in sorted(TESTS.glob('*-input.txt')):
    case=p.name.removesuffix('-input.txt')
    expected=p.with_name(case+'-expected.txt').read_text(encoding='utf-8')
    level,seed=case.split('-',1)[1].split('_')
    header_level=re.search(r'^Level: (\w+)',expected,re.M).group(1).lower()
    header_seed=re.search(r'^Random generator initialized with seed: (\d+)',expected,re.M).group(1)
    configs=[('filename',level,seed)]
    if (level,seed)!=(header_level,header_seed): configs.append(('expected-header',header_level,header_seed))
    for mode,lv,sd in configs:
        r=run([str(JDK/'java.exe'),'-Dfile.encoding=UTF-8','-Dstdout.encoding=UTF-8','-Dstderr.encoding=UTF-8','-cp',str(CLASSES),'pvz.PlantsVsZombies',lv,sd],p.read_text(encoding='utf-8'))
        tag=case if mode=='filename' else case+'-'+mode
        (OUT/(tag+'-actual.txt')).write_text(r.stdout,encoding='utf-8')
        (OUT/(tag+'-stderr.txt')).write_text(r.stderr,encoding='utf-8')
        diff='\n'.join(difflib.unified_diff(expected.splitlines(),r.stdout.splitlines(),fromfile='expected',tofile='actual'))
        (OUT/(tag+'-diff.txt')).write_text(diff,encoding='utf-8')
        cycles=lambda s: re.findall(r'Number of cycles: (\d+)',s)
        errors=lambda s: [x for x in s.splitlines() if '[ERROR]' in x]
        summary.append(dict(case=case,configuration=mode,level=lv,seed=sd,exit=r.returncode,match=not diff,last_cycle_expected=cycles(expected)[-1],last_cycle_actual=cycles(r.stdout)[-1],errors_expected=errors(expected),errors_actual=errors(r.stdout),expected_tail=expected.splitlines()[-4:],actual_tail=r.stdout.splitlines()[-4:],expected_lines=len(expected.splitlines()),actual_lines=len(r.stdout.splitlines())))
(OUT/'summary.json').write_text(json.dumps(summary,ensure_ascii=False,indent=2),encoding='utf-8')
for s in summary: print(json.dumps(s,ensure_ascii=False))
