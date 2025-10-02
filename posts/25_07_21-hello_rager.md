---
title: Hello RAGer
description: Introducing RAGer, a thin library for generative models that abstracts over providers and modalities in Ruby and Elixir.
---

I've been working on using generative models in my day-to-day tasks and trying new things that weren't possible before. When looking at existing libraries, the big one is LangChain, which I've used before and personally didn't like because it felt like I was writing a LangChain program instead of a Python program. There are newer ones in Python and most languages have their own version, but they usually take a lot of inspiration from LangChain. I would like to give credit to [567-labs/instructor](https://github.com/567-labs/instructor) in Python and [thmsmlr/instructor_ex](https://github.com/thmsmlr/instructor_ex) in Elixir, as these focus on structured outputs with LLMs and work nicely with your existing schemas and code.

I could have used InstructorEx in my Elixir Phoenix applications and been happy, but I wanted to create my own library to experiment with. Also, while you can track the inputs and outputs of each step and user outcomes in your application database, I wanted to build an accompanying logging server once and use it across apps to iterate quickly. Lastly, I think that other modalities like image and audio generation are going to be used heavily alongside LLMs and I want a library to cover those as well. For these reasons I built `rager`, which provides a thin layer over HTTP calls and abstracts over providers and modalities. The main implementation is in Ruby and has functions that let you start simple like `ctx.chat("Tell me a joke")` but then allow you to use more advanced features like streaming and structured outputs. Here is a snippet from the `README`:

```ruby
ctx = Rager::Context.new

prompt = ctx.template("Tell me about the history of <%= topic %>", { 
  topic: PROMPT 
})

chat = ctx.chat(prompt, stream: true)

chat.stream.each { |d| print d.content }
```

And here is a more advanced example:

```ruby
ctx = Rager::Context.new(max_retries: 3)

search_terms = ctx.chat(
  ctx.template(REWRITE_TEMPLATE, { 
    prompt: PROMPT 
  }, provider: :mustache)
)

results = ctx.search(search_terms, n: 5, max_length: 3000)

ranked = ctx.rerank(search_terms, 
  results.let { |r| r.map(&:content) }
)

recommendations = ctx.chat(
  ctx.template(ANSWER_TEMPLATE, {
    prompt: PROMPT,
    results: ranked.let { |r| r.map(&:content) }
  }, provider: :mustache)
)

ctx.template(FINAL_TEMPLATE, {
  response: recommendations,
  sources: results.let { |r| 
    r.each_with_index.map { |item, i| { 
      url: item.url, 
      index: i + 1 
    } 
  }
}, provider: :mustache)
```

One benefit of being a thin layer is that it is easy to vibe code implementations in other languages, which I have done for Elixir. I wanted the library to have as little magic as possible, but after testing there are a few things implemented for convenience. The first is that each of the top level functions returns a `Result` struct and you can pass that as any input to another function. When you do that, the output value is extracted and the input IDs are passed through to the next function. This is used to build a DAG of operations on the server to do things like show the structure of your workflow in the UI. Also, there is the `let` function for when you want to apply some transformation to the `value` of a result while preserving the flow of input IDs. And you can manually pass in the context and input IDs so you can persist them in the database and start and stop workflows at any time.

The main implementation is in Ruby here: [mvkvc/rager_rb](https://github.com/mvkvc/rager_rb) with tests and the smaller Elixir version will be at [mvkvc/rager](https://github.com/mvkvc/rager). To use the Ruby version, add the following to your Gemfile: `gem 'rager_rb', git: 'https://github.com/mvkvc/rager_rb.git', branch: 'main'`. For the Elixir version, add this to your mix.exs deps: `{:rager, git: "https://github.com/mvkvc/rager.git", branch: "main"}`.
