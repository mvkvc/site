import Config

config :still,
  dev_layout: false,
  pass_through_copy: ["assets/fontello"],
  input: Path.join(Path.dirname(__DIR__), "priv/site"),
  output: Path.join(Path.dirname(__DIR__), "_site")

config :tailwind,
  version: "3.2.4",
  default: [
    args: ~w(
      --config=tailwind.config.js
      --input=css/input.css
      --output=css/output.css
    ),
    cd: Path.expand("../priv/site/assets", __DIR__)
  ]

import_config("#{Mix.env()}.exs")
