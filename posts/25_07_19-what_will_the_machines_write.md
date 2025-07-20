---
title: What will the machines write
---

I've decided to start writing about my projects. This is the first post, so it's about the website it's posted on. It's a simple markdown file based site I built yesterday with Kotlin and Ktor. This is the first time I've used Kotlin, my usual go to language is Elixir, I've been learning Rust, and I use Python/TypeScript when necessary. 

Recently I've been thinking about what languages are best for generative software development. The characteristics I've come up with are being readable, has strong static types, is garbage collected, and a strong ecosystem is a plus. I want that when the generated code compiles it holds certain invariants and won't break my application. I also need to be able to jump into a large generated codebase and understand it at a glance. Testing is useful to be generated as well but you have to review the tests before you can rely that they are catching anything worthwhile. However if you define some abstractions in the core that you know the compiler is going to enforce, and those files haven't changed, you can rely on that to a greater extent.

For these reasons I am learning Kotlin, it's type system seems to be expressive enough for my needs while being straightforward to understand. I still plan to use Elixir and Rust for their strengths but as a default backend language I am starting here with my personal software. My prediction is that I will be deleting more generated code than I write by the end of the year.
