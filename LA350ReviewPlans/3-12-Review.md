# COCS350 Review - 3/12

## Concept Map

```text
Linux + C
|
+-- Directory layout and libraries
+-- Bash scripting
+-- System calls
|   |
|   +-- read / write
|   +-- lseek / pread / pwrite
|
+-- File stats and i-nodes
+-- File sharing with dup / dup2
```

## Linux and C Overview

### Big Idea

```text
User program
   |
   | library call (often buffered)
   v
System call
   |
   v
Kernel
   |
   v
Hardware / files / devices
```

| Term | Very short meaning |
| --- | --- |
| User mode | Normal program mode, limited access |
| Kernel mode | Privileged mode, direct resource access |
| System call | Safe gateway from user program to kernel |
| Library call | User-space helper, often built on system calls |

### Key Points

- System calls are used when a program needs kernel-managed resources.
- System calls cost more than normal function calls because of mode switching.
- Library calls are often faster for repeated output because they use buffers.

### Memory Aid

- `library call = convenience`
- `system call = kernel access`

## Directory Layout and Libraries

### Common Directories

| Path | Purpose |
| --- | --- |
| `/bin` | Essential commands/binaries |
| `/dev` | Device files |
| `/etc` | System configuration files |
| `/home` | User home directories |
| `/boot` | Boot loader and kernel-related files |
| `/root` | Root user's home |
| `/usr` | User programs, docs, headers, libraries |
| `/lib` | Essential shared libraries and modules |

### Where Programming Files Usually Live

| Item | Typical location |
| --- | --- |
| Compiler programs | `/usr/bin`, sometimes `/usr/local/bin` |
| Header files | `/usr/include` |
| System headers | `/usr/include/sys` |
| Libraries | `/lib`, `/usr/lib` |

### Compiler Flags to Know

| Flag | Meaning | Example |
| --- | --- | --- |
| `-I` | Add header search path | `gcc -I/usr/openwin/include fred.c` |
| `-L` | Add library search path | `gcc -L. program.c -lfoo` |
| `-l` | Link a library by short name | `-lm` means math library |

### Static vs Shared Libraries

| Type | Suffix | What happens |
| --- | --- | --- |
| Static library | `.a` | Code is copied into executable |
| Shared library | `.so` | Code is loaded/shared at runtime |

```text
program + static library  -> bigger executable, more duplicate code
program + shared library  -> smaller executable, shared at runtime
```

### Static Library Build Flow

```text
fred.c + bill.c
   |
   v
gcc -c fred.c bill.c
   |
   v
fred.o + bill.o
   |
   v
ar crv libfoo.a fred.o bill.o
   |
   v
gcc -L. program.o -lfoo
```

## Bash Shell Programming

### Variable Basics

| Rule | Example |
| --- | --- |
| No spaces around `=` | `x=3` |
| Use `$` to read value | `echo $x` |
| Use quotes for spaces | `msg="how are you"` |
| Double quotes expand variables | `echo "$x"` |
| Single quotes keep text literal | `echo '$x'` |

### Useful Commands

| Command | Use |
| --- | --- |
| `read name` | Get user input |
| `readonly x` | Make variable read-only |
| `unset x` | Remove variable |
| `export x=value` | Send variable to child processes |

### Special Variables

| Variable | Meaning |
| --- | --- |
| `$0` | Script name |
| `$#` | Number of arguments |
| `$1`, `$2`, ... | Positional arguments |
| `$@` | All arguments, separately |
| `$*` | All arguments as one string when quoted |
| `$?` | Last exit status |
| `$$` | Script process ID |

### Condition Tests

| Category | Tests |
| --- | --- |
| Strings | `=`, `!=`, `-n`, `-z` |
| Integers | `-eq`, `-ne`, `-gt`, `-ge`, `-lt`, `-le` |
| Files | `-e`, `-d`, `-f`, `-r`, `-w`, `-x`, `-s` |

### Control Structures

```text
if [ condition ]; then ... fi
if [ condition ]; then ... else ... fi
if [ cond1 ]; then ... elif [ cond2 ]; then ... else ... fi
case value in pattern) ... ;; esac
```

### Loops at a Glance

| Loop | When it continues |
| --- | --- |
| `for` | Over a list or counter |
| `while` | While condition is true |
| `until` | While condition is false |

```sh
for os in Linux Windows "MAC OS"; do
  echo "$os"
done

while [ "$password" != "secret" ]; do
  read password
done
```

### Memory Aid

- `while = keep going when true`
- `until = keep going when false`
- `case = cleaner than many ifs`

## Bash Functions

### Function Shape

```sh
name() {
  local x=5
  echo "$x"
}
```

### Core Rules

| Rule | Meaning |
| --- | --- |
| Define before call | Shell must know the function first |
| Variables are global by default | Use `local` to limit scope |
| Arguments use `$1`, `$2`, `$@` | Same style as scripts |
| `return` is status-only | Must fit in `0` to `255` |

### Useful Built-Ins

| Tool | Use |
| --- | --- |
| `expr` | Arithmetic/comparison with spaces around operators |
| `${#x}` | String length |
| `${x:3}` | Substring from index 3 |
| `${x:3:2}` | Substring of length 2 starting at index 3 |
| `printf` | Formatted output |

### Quick Warning

```text
return 45   -> fine
return 300  -> wraps/truncates to shell status range
```

## C File-Management System Calls

### Main Calls

| Call | Purpose |
| --- | --- |
| `read(fd, buf, nbyte)` | Read bytes from a file descriptor |
| `write(fd, buf, nbyte)` | Write bytes to a file descriptor |
| `lseek(fd, offset, whence)` | Move file offset |
| `pread(fd, buf, count, offset)` | Read at offset without changing current offset |
| `pwrite(fd, buf, count, offset)` | Write at offset without changing current offset |

### Standard File Descriptors

| FD | Meaning |
| --- | --- |
| `0` | Standard input |
| `1` | Standard output |
| `2` | Standard error |

```text
keyboard  -> 0
screen    -> 1
errors    -> 2
```

### `read` and `write` Rules

- Always check the return value.
- They may read or write fewer bytes than requested.
- `-1` usually means an error.

### `lseek` Visual

```text
File:  [abcdefghij......................ABCDEFGHIJ]
Index: 0         10                    40        50

write 10 bytes -> offset becomes 10
lseek to 40    -> gap/hole from 10 to 39
write 10 bytes -> offset becomes 50
```

### `whence` Values

| Constant | Meaning |
| --- | --- |
| `SEEK_SET` | From beginning of file |
| `SEEK_CUR` | From current position |
| `SEEK_END` | From end of file |

### `pread` / `pwrite` Idea

```text
lseek + read/write       -> changes shared offset
pread / pwrite           -> uses a specific offset, leaves current offset alone
```

- This matters when multiple threads use the same open file descriptor.

### File Permissions

```text
u = user owner
g = group owner
o = others

r = read
w = write
x = execute
```

- New file permissions depend on base permissions and `umask`.

## Linux File Stats

### Filesystem Picture

```text
Directory entry -> filename + i-node number
                     |
                     v
                  i-node
                     |
                     +-- file type
                     +-- permissions
                     +-- owner/group
                     +-- size
                     +-- timestamps
                     +-- block pointers
```

### Big Ideas

- A directory is a special file containing names and i-node references.
- The i-node stores metadata, not the filename itself.
- A file is deleted when its hard-link count reaches `0` and it is no longer needed.

### `stat` Family

| Call | What it checks |
| --- | --- |
| `stat(path, &sb)` | Path target |
| `fstat(fd, &sb)` | Open file descriptor |
| `lstat(path, &sb)` | Symbolic link itself |

### Important `struct stat` Fields

| Field | Meaning |
| --- | --- |
| `st_mode` | File type + permissions |
| `st_ino` | I-node number |
| `st_nlink` | Number of hard links |
| `st_uid` | Owner ID |
| `st_gid` | Group ID |
| `st_size` | Size in bytes |
| `st_atime` | Last access time |
| `st_mtime` | Last modification time |
| `st_ctime` | Last status-change time |

### File Type Macros

| Macro | File type |
| --- | --- |
| `S_ISREG` | Regular file |
| `S_ISDIR` | Directory |
| `S_ISCHR` | Character special file |
| `S_ISBLK` | Block special file |
| `S_ISFIFO` | FIFO |
| `S_ISLNK` | Symbolic link |
| `S_ISSOCK` | Socket |

### Hard Link Visual

```text
dir A: report.txt  ----\
                        > same i-node -> same file data
dir B: copyname    ----/
```

- `link(existing, new)` creates another directory entry pointing at the same i-node.

## File Sharing

### Kernel Structures

```text
Process A descriptor table      Process B descriptor table
fd 3 ----------------------\    fd 4 ----------------------\
                            \                              \
                             v                              v
                          Open file table entry
                          - current offset
                          - status flags
                          - pointer to vnode/i-node
```

### What Gets Shared?

| Shared item | Shared or not? |
| --- | --- |
| File offset | Shared if descriptors point to same open file entry |
| Status flags | Shared if descriptors point to same open file entry |
| Descriptor number | Not necessarily shared |

### `dup` and `dup2`

| Call | Meaning |
| --- | --- |
| `dup(fd)` | Copy to lowest unused descriptor |
| `dup2(oldfd, newfd)` | Copy onto exactly `newfd` |

### Redirection Visual

```text
Before dup2(newfd, 1):
1 -> terminal

After dup2(newfd, 1):
1 -> file
```

### Why This Matters

- Shared descriptors make shell redirection and pipelines possible.
- Parent and child processes can inherit open files after `fork`.
- Exported environment variables are also inherited by child processes.

## Last-Minute Checklist

- Know what directories like `/bin`, `/etc`, `/usr`, and `/lib` are for.
- Know the difference between `.a` and `.so`.
- Know shell variable syntax and special variables.
- Know `if`, `case`, `for`, `while`, and `until`.
- Know `local`, `return`, `printf`, and `export`.
- Know what `read`, `write`, `lseek`, `pread`, and `pwrite` do.
- Know what `stat`, `fstat`, and `lstat` do.
- Know what an i-node stores.
- Know what `dup` and `dup2` share.
