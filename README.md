Mason Engineering - Embedded Engineer exercise
===

# "Memallook" - a visual memalloc simulation

Your objective is to write a simple command-line tool named `memallook` in one of the languages
shown below. This tool's primary job is to maintain a stateful record of memory allocations performed
in a buffer and display it when requested.

When run for the first time, the tool expects two parameters like so:
`memallook p N`, where:
* `p` is the page size in bytes
* `N` is the number of available pages

These parameters together give you the total available memory space as `N * p`.

Subsequent invocations can be in one of these forms:
1. `memallook alloc M`
2. `memallook dealloc T`
3. `memallook show`
4. `memallook clear`

The first command `alloc` "allocates" a block of `M` bytes in the memory buffer and returns
a unique tag `T` which sort of can be treated like a pointer. If `M` bytes can no longer be
allocated, the tool returns an error.

The second command `dealloc` deallocates a previously allocated block with tag `T`. If `T` is
unknown, this is an error.

The third command displays the state of the memory on the terminal using a simple character matrix
with an `x` displaying an occupied page, and a tag character implying a free page (see example below).
This grid/matrix is then followed by a listing of all tags allocated and the number of bytes
allocated for each.

The last form clears any maintained state and starts over, causing the tool to expect values
for `p` and `N` for the next invocation.

## Example

Here is an example run of the tool:

```
$ memallook 16 64
ok, buffer of size 1024 bytes created

$ memallook alloc 16
ok, tag = "1"

$ memallook alloc 2048
allocation failed

$ memallook show
1...............
................
................
................

<Allocations by tag>
1: 16 bytes

$ memallook alloc 32
ok, tag = "2"

$ memallook show
122.............
................
................
................

<Allocations by tag>
1: 16 bytes
2: 32 bytes

$ memallook dealloc 1
deallocation succeeded

$ memallook dealloc 99
deallocation failed, unknown tag

$ memallook show
.22.............
................
................
................

<Allocations by tag>
2: 32 bytes

```

NOTES:
* Display matrix sizing can be chosen arbitrarily and does not need to match
  the example above
* The tag uses strings "1", "2", etc. This is also only a suggestion


# Stretch Addon 1

> NOTE: This is purely optional and will not affect scoring or the interview
> evaluation process. We understand many candidates have existing jobs
> demanding their attention.

If you reach this stage, consider adding support for a command `defrag` that
will perform defragmentation of the memory buffer if possible.

## Example

```
$ memallook 16 64
ok, buffer of size 1024 bytes created

$ memallook alloc 160
ok, tag = "1"

$ memallook alloc 2048
allocation failed

$ memallook show
1111111111......
................
................
................

<Allocations by tag>
1: 160 bytes

$ memallook alloc 32
ok, tag = "2"

$ memallook show
111111111122....
................
................
................

<Allocations by tag>
1: 160 bytes
2: 32 bytes

$ memallook dealloc 1
deallocation succeeded

$ memallook show
..........22....
................
................
................

<Allocations by tag>
2: 32 bytes

$ memallook defrag
defragmentation complete

$ memallook show
22..............
................
................
................

<Allocations by tag>
2: 32 bytes

```

# Submission

You are to build a small program that can be run from the command line as described above.
You may use any of the following languages:

* C/C++
* Golang
* Rust

If you have a different one you prefer, please contact your interviewer.

## DOs
* Fork this repo to your own user space, and add @scorpiodawg and/or @trevorhalvorson as an
  outside collaborator. If you prefer to keep your work private, you may [duplicate](https://help.github.com/en/articles/duplicating-a-repository)
  it instead and add us as collaborators.
* Push all changes to the `master` branch of your fork, and let us know when you're ready
  to officially submit. We will fork your repo and review your code. Changes made after that
  will likely be ignored, so please do submit only after you are ready
* Do use any popular, modern language and technology stack of your choice.
* Do ensure your final submission includes everything so that the project can be
  **built, deployed, runnable and accessible locally**.
* Do write self-documenting code
* Do include a `SOLUTION.md` with the following:
  * what major design decisions did you have to make
  * why did you choose any frameworks or libraries used
  * a references section listing any _significant_ resources used during development
    (significant StackOverflow questions, articles, books, etc. You do not need to
    include links to questions about language syntax, for e.g.)
* Do implement your solution as though you intend to productionize it eventually

## DONTs
- Don't jump right in if you have any doubts as to whether your choice of language/
  framework might not be suitable for our review (e.g. less common ones such as
  Clojure, Fortran, Elixir, etc.). Please contact us ahead of time to clarify.

## Timing
We expect this project to take approximately 2-6 hours of your time. However,
everyone is different so please treat these numbers as suggestions. If you find
yourself blocked, or stuck spending inordinate amounts of time in a specific area,
it might be best to reach out to us -- we want to be respectful of your time and
it is our job to ensure you have a pleasant experience.

## Feedback

We are always looking for feedback on the question itself. If, after reading through the
problem, you have questions on its clarity or suggestions on how to improve it, we're all
ears! You may leave a file named `FEEDBACK.md` when you submit your code.

**We look forward to seeing your work!**
