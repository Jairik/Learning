# COCS350 Practice Problems - 3/12

Use these as quick checks. Try to answer before opening each dropdown.

## Directory Layout and Libraries

1. Where are C header files usually stored on Linux?

<details>
<summary>Answer</summary>

Usually in `/usr/include` and its subdirectories, such as `/usr/include/sys`.

</details>

2. What is the difference between a `.a` library and a `.so` library?

<details>
<summary>Answer</summary>

`.a` is a static library linked into the executable. `.so` is a shared library loaded at run time.

</details>

3. What do `-I`, `-L`, and `-l` do in `gcc`?

<details>
<summary>Answer</summary>

`-I` adds a header search path, `-L` adds a library search path, and `-l` links a library by name.

</details>

## Shell Programming

4. Why is `x = 3` wrong in shell, but `x=3` is correct?

<details>
<summary>Answer</summary>

Shell assignment cannot have spaces around `=`.

</details>

5. What is the difference between `"$@"` and `"$*"`?

<details>
<summary>Answer</summary>

`"$@"` keeps arguments separate; `"$*"` combines them into one string.

</details>

6. What does `$?` store?

<details>
<summary>Answer</summary>

The exit status of the most recently executed command or function.

</details>

7. Which test checks whether a file exists: `-e`, `-d`, or `-x`?

<details>
<summary>Answer</summary>

`-e`.

</details>

8. Which loop continues while its condition is false?

<details>
<summary>Answer</summary>

`until`.

</details>

## Shell Functions

9. Are shell variables local or global by default?

<details>
<summary>Answer</summary>

Global by default.

</details>

10. What keyword makes a variable local inside a shell function?

<details>
<summary>Answer</summary>

`local`.

</details>

11. Why should you not use `return` for large numeric results in shell functions?

<details>
<summary>Answer</summary>

Because shell function return values are limited to status codes in the range `0` to `255`.

</details>

12. What does `${#x}` do?

<details>
<summary>Answer</summary>

It returns the length of string `x`.

</details>

## System Calls

13. Why do user programs use system calls instead of directly touching hardware?

<details>
<summary>Answer</summary>

System calls provide controlled kernel access to low-level resources while keeping normal programs in user mode.

</details>

14. What are file descriptors `0`, `1`, and `2`?

<details>
<summary>Answer</summary>

Standard input, standard output, and standard error.

</details>

15. What does `lseek()` change?

<details>
<summary>Answer</summary>

It repositions the current file offset for an open file.

</details>

16. What is the main advantage of `pread()` and `pwrite()` over `lseek()` plus `read()`/`write()`?

<details>
<summary>Answer</summary>

They do offset-based I/O without changing the shared file offset, which helps avoid races.

</details>

17. What does `umask` affect?

<details>
<summary>Answer</summary>

It affects the default permissions assigned to newly created files and directories.

</details>

## File Stats and Links

18. What does an i-node store: file contents, file metadata, or both names and metadata?

<details>
<summary>Answer</summary>

File metadata.

</details>

19. Which call reports information about a symbolic link itself: `stat`, `fstat`, or `lstat`?

<details>
<summary>Answer</summary>

`lstat`.

</details>

20. What does `st_nlink` represent?

<details>
<summary>Answer</summary>

The number of hard links pointing to the file's i-node.

</details>

21. Which macro checks whether a file is a directory?

<details>
<summary>Answer</summary>

`S_ISDIR`.

</details>

22. When is a file actually deleted from the filesystem?

<details>
<summary>Answer</summary>

When its link count reaches `0` and no process still needs it open.

</details>

## File Sharing

23. What does `dup(fd)` return?

<details>
<summary>Answer</summary>

A copy of the file descriptor using the lowest-numbered available descriptor.

</details>

24. What is shared when two descriptors refer to the same open file entry?

<details>
<summary>Answer</summary>

The file offset and file status flags.

</details>

25. Why is `dup2(newfd, 1)` useful?

<details>
<summary>Answer</summary>

It redirects standard output to `newfd`, which is a common way to send output into a file.

</details>

26. Give one real reason parent and child processes need file sharing.

<details>
<summary>Answer</summary>

To support redirection, pipelines, or inherited open files after `fork`.

</details>
