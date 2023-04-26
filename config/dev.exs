import Config

config :still,
  dev_layout: true,
  profiler: false,
  watchers: [
    mix: ["tailwind", "default", "--watch"]
  ]
