import { defineConfig } from 'astro/config';
import svelte from "@astrojs/svelte";
import tailwind from "@astrojs/tailwind";
import mdx from "@astrojs/mdx";

export default defineConfig({
  site: 'https://mvk.vc',
  markdown: {
    syntaxHighlight: 'prism',
  },
  integrations: [svelte(), tailwind(), mdx()]
});