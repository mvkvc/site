---
title: What will the machines write
---

I've decided to start writing about my projects. This is the first post, so it's about the website it's posted on. It's a simple markdown file based site I built yesterday with Kotlin and Ktor. This is the first time I've used Kotlin, my usual go to language is Elixir, I've been learning Rust, and I use Python/TypeScript when necessary. 

Recently I've been thinking about what languages are best for building software with generative models. The characteristics I've come up with are being readable, having a strong static type system, being garbage collected, and a large ecosystem is a plus. I want that when the generated code compiles it holds certain invariants and won't break my application. I also need to be able to jump into large generated codebases that have gone off the rails and understand them at a glance. Tests are useful to be generated as well but you have to review them to see if they catch anything worthwhile. Conversely if you define some abstractions in the core that the compiler enforces, and those files haven't changed, you can rely on that to a greater extent.

For these reasons I am trying out Kotlin, it's type system seems to be expressive enough while being straightforward to understand. I still plan to use Elixir and Rust for their strengths but as a default backend language I am starting here with my personal software. My prediction is that I will be deleting more generated code than I write by the end of the year.
