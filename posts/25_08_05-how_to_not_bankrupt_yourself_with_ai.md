---
title: How to not bankrupt yourself with LLMs
---

I use LLMs heavily when writing code because most code isn't original, and I want to focus on hard problems. I don't "vibe code" like people describe, I decide on the stack and approach, then instruct the LLM at a high level. I also review a lot of my existing code and ask questions, which involves reading many files, so I use a lot of tokens and have figured out a workflow for myself so I don't go into debt accidentally.

In a coding workflow, here are the different model classes:

1.  **Planning models** - High-level design and problem solving. Can use reasoning models for solid plans.
2.  **Action models** - Implementation and code generation. Need speed for iteration, with cost as a significant factor due to heavy tool usage.
3.  **Apply models** - Fast models for applying discussed changes to files. More efficient than using the action model for each change.
4.  **FIM models** - Tab autocomplete. Small, fast models for quick suggestions - the most frequently used AI coding feature.

Using the same model for all roles is not ideal at least speed wise for apply and FIM. I recommend keeping planning and action models consistent to maintain approach and style, but choose the models yourself as benchmarks aren't reliable indicators of real-world performance.

I use Cline for plan/act modes and Continue for autocomplete. Z.AI provides a coding agent plan that works well with Claude Code and this is the best CLI agent setup that I have tried, also I use Qwen Code for Cerebras when I want to use those tokens in the CLI.

## Recommended Models

**Planning:**
1. Qwen 3 Coder
2. GLM 4.5
3. DeepSeek V3

**Action:**
1. Qwen 3 Coder
2. GLM 4.5/4.5 Air
3. DeepSeek V3

**Apply:**
1. Gemini 2.5 Flash
2. Gemini 2.5 Flash Lite
3. Morph V3 Large

**FIM:**
1. Mercury Coder
2. DeepSeek V3 (FIM)
3. Qwen 2.5 Coder 1.5B (via Ollama)

My current workflow uses Qwen 3 Coder for both plan and act modes, with Gemini 2.5 Flash for applying changes when possible, and Mercury Coder for autocomplete. I prefer to pick an open source model and use it across multiple providers to maintain consistency in my approach and prompts.

## My Current Setup

Here's my actual daily setup that keeps costs under control while providing massive token capacity:

**Primary Stack:**
- **Cerebras Inference**: 24 million tokens per day for just $50/month
- **Z.AI $3/month plan**: Claude Code access with GLM 4.5 and GLM 4.5 Air models (subsidized, but even at API prices across different providers it's still sustainable unlike Anthropic)
- **OpenRouter**: Backup for when I hit limits (which takes a while with the above setup)

This combination gives incredible value and lets me code freely without worrying about token costs. The Z.AI plan has to be used from existing coding agents and so the Cerebras plan gives me both enough tokens for use of my own applications as well.
