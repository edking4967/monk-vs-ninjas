import re
from os import path

files = open('javalist.txt')
outdir = "classes"
p = re.compile(r'''((synchronized +)?
            (public|private|protected)\s+
            (static\s[a-zA-Z\[\]]+|[a-zA-Z\[\]]+)\s
            [a-zA-Z]+\([a-zA-Z ,\[\]]*\))
            \n?[a-zA-Z ,\t\n]*\{''', re.VERBOSE)

for fn in files.readlines():
    fn = fn.split('\n')[0]
    f = open(fn)
    name = fn.split('/')[-1]
    methods = p.findall(f.read())

    f.close()
    out = ""
    for method in methods:
        out += method[0] + "\n"
    o = open(path.join(outdir, name), 'w')
    o.write(out)
    o.close()

files.close()
