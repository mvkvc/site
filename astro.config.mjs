import { defineConfig } from 'astro/config';
import svelte from "@astrojs/svelte";
import tailwind from "@astrojs/tailwind";
import mdx from "@astrojs/mdx";

// https://astro.build/config
export default defineConfig({
  site: 'https://mvk.vc',
  markdown: {
    syntaxHighlight: 'prism',
  },
  integrations: [svelte(), tailwind(), mdx()]
});