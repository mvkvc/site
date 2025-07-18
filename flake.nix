{
  inputs = {
    nixpkgs.url     = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
        };
      in
      {
        devShells.default = with pkgs; mkShell {
          buildInputs = [
            temurin-bin-17
          ];
          shellHook = ''
            export JAVA_HOME="${pkgs.temurin-bin-17}
            export ANDROID_HOME="$HOME/Android/Sdk"
          '';
        };
      }
    );
}
