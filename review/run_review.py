from pathlib import Path
import subprocess, json, difflib

ROOT = Path(__file__).resolve().parent.parent
OUT = ROOT / 'review'
JAVA = Path('C:/Program Files/Java/jdk-25.0.4/bin')
CLASSES = OUT / 'classes'
CLASSES.mkdir(exist_ok=True)

def run(args, data=None):
    return subprocess.run(args, input=data, capture_output=True, text=True,
                          encoding='utf-8', errors='replace', timeout=30, cwd=ROOT)

sources = list((ROOT/'PlantillaPR1CV/src').rglob('*.java'))
c = run([str(JAVA/'javac.exe'), '-encoding', 'UTF-8', '-Xlint:all', '-d', str(CLASSES)] + [str(p) for p in sources] + [str(OUT/'ReviewChecks.java')])
(OUT/'compile.txt').write_text(c.stdout+c.stderr, encoding='utf-8')
if c.returncode:
    raise SystemExit(c.stderr)
r = run([str(JAVA/'java.exe'), '-cp', str(CLASSES), 'pvz.logic.ReviewChecks'])
(OUT/'results.txt').write_text(r.stdout+r.stderr, encoding='utf-8')
print(r.stdout)

summary=[]
for path in sorted((ROOT/'PlantillaPR1CV/src/test/resources').glob('*-input.txt')):
    case=path.stem.removesuffix('-input')
    difficulty, seed = case.split('-',1)[1].split('_')
    r=run([str(JAVA/'java.exe'),'-cp',str(CLASSES),'pvz.PlantsVsZombies',difficulty,seed], path.read_text(encoding='utf-8'))
    (OUT/(case+'-actual.txt')).write_text(r.stdout+r.stderr,encoding='utf-8')
    expected=path.with_name(case+'-expected.txt').read_text(encoding='utf-8')
    diff=list(difflib.unified_diff(expected.splitlines(),r.stdout.splitlines(),fromfile='expected',tofile='actual'))
    (OUT/(case+'-diff.txt')).write_text('\n'.join(diff),encoding='utf-8')
    summary.append(dict(case=case,exit=r.returncode,exact_match=not diff,first_diff=diff[:12]))

cli=[]
for args in [[],['bad'],['easy','x'],['easy','25','extra'],['easy','25']]:
    r=run([str(JAVA/'java.exe'),'-cp',str(CLASSES),'pvz.PlantsVsZombies']+args,'exit\n')
    cli.append(dict(args=args,exit=r.returncode,stderr=r.stderr,output=r.stdout))
(OUT/'integration.json').write_text(json.dumps(dict(legacy_traces=summary,cli=cli),ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(summary,ensure_ascii=False,indent=2))
print('CLI:',[(x['args'],x['exit']) for x in cli])
