---
title: Reimplementing my agent's to-do list
description: Creating an MCP server and CLI for hierarchical task tracking using Python, FastMCP, and SQLite.
---

Recently on X I saw someone describing their workflow with [eyaltoledano/claude-task-master](https://github.com/eyaltoledano/claude-task-master) where they left their agent generating and completing tasks in a loop. While it wasn't for me, it made me want to be more intentional with tasks and planning when coding with agents.

Up until now, I usually maintained a freeform `TODO.md` bullet list, but it ends up getting chaotic. When you want to track status and other fields, you end up reimplementing your own table. I tried using a markdown table instead, but it's not very convenient to add or edit entries even though the model can do it for you and there's no enforced structure for the data. At a certain point, you want to query the data, and then you might as well be using SQLite.

However, SQLite databases are binary and probably shouldn't be stored in git, which doesn't work when you want to spawn potentially large numbers of agents that each work on different tasks. So I decided to use Python and [simonw/sqlite-diffable](https://github.com/simonw/sqlite-diffable) to load and persist the database to a human-readable `.ndjson` file. After doing most of the work, I looked at the source of `sqlite-diffable` and realized how little code it is. I could have probably picked another language if I wanted to.

In any case, writing MCP servers isn't very interesting, and I wanted to get it done quickly and move on with my life. I can't keep making todo lists forever. So it's a FastMCP server (aka FastAPI with some decorators) using SQLAlchemy and SQLite to store the tasks. Here's what it looks like in Cline:

<img src="/static/images/taskhelper_mcp.png" alt="taskhelper_mcp" style="max-width: 400px; width: 100%; height: auto; display: block; margin: 0 auto;">

And because sometimes the model gets it wrong or you want to manually CRUD some entries, I added a CLI using Click that exposes the same commands as the MCP server. Here's what listing some tasks looks like:

![taskhelper](/static/images/taskhelper.png)

As usual, Python with FastAPI is nice to use, though I unfortunately spent most of my time on the database with SQLAlchemy and Alembic. I have to give credit to [astral-sh/uv](https://github.com/astral-sh/uv) for making Python MCP servers much easier by letting people run applications directly with `uvx`.

You can find the code at [mvkvc/taskhelper](https://github.com/mvkvc/taskhelper), with instructions in the `README.md` on what to copy into your MCP server config to get it running with no setup. If you're using it in VSCode, make sure to launch VSCode from your project folder instead of your toolbar otherwise it will get the wrong root folder.
