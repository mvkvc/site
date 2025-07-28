---
title: Reimplementing my to-do list
---

For the applications I use often, I always think about how hard it would be to recreate them. Now, with generative models, it's more fun to do and also a good benchmark to see how good the agents are getting. As I am working on my own coding agent, these kinds of applications are also great to evaluate performance on self-contained projects. While I work on getting that out, I wanted to start using my own kanban-style to-do list without having to transfer the data over later, so I used Cline to reimplement it.

Here is a screenshot: 

![kanban](/static/images/kanban.png)

It's a React SPA frontend with a Rust API backend that uses Axum and Diesel. If I were to do it again today, I would not use React because I feel like I am learning React instead of the web platform itself, which is what I want to invest in long-term. If I am using a framework, I would prefer something like HTMX that I can reuse across server-side languages, or Vue which leans more into HTML. To be fair, most of the interesting parts of my projects are in the backend, and the frontend is usually just a thin layer to expose them.

For the backend, Rust is great for coding with generative models. The type system and the compiler are very helpful, and this is the project that first gave me the impression that a strong type system is important for generated code. In the majority of cases, when the code compiled and ran, it was correct, and when it didn't the compiler feedback loop usually led the way to a solution. If my company had its main products in Rust, I could see using it for this type of CRUD backend, but otherwise, I would prefer another statically typed backend language like Kotlin or Go. I will probably rewrite this to other languages in the future as a test of different coding agents, but for now it works, and you the code at [mvkvc/kanban](https://github.com/mvkvc/kanban). I look forward to doing the same for other applications I use and building up a personal ecosystem of tools.
