---
title: Crossroads
---

This summer has come to a close, and reflecting back on this year much of my work was exploratory and I started many new projects. I'm happy with most of them, but now I want to commit to a select few to complete and iterate on for the rest of the year. The goal is to enter the new year ready to go all-in on a single project while reaping the benefits of the others in those efforts.

I'll be focusing my efforts on two key themes:

- AI-enabled apps with feedback loops
- Sim-to-real for robotics

Here are the projects I'm prioritizing to stabilize:

- An LLM inference coordination server
- An LLM workflow library
- A logging and feedback server for the workflow library
- A coding agent
- A service that turns prompts/images into 3D prints mailed to you

And here is the project I want to continue now and focus on in the new year:

- A sim-to-real system for drones built in tandem with the hardware

The default setup uses GitHub hosting with GitHub Actions for CI/CD deployed to AWS EKS, CDKTF with TypeScript for infrastructure as code, Aurora serverless Postgres with PGVector for the database, and a stack of Elixir with Kotlin, Rust, TypeScript, and Python where needed.
