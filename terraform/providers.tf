terraform {
  required_version = ">= 1.6.0"
  backend "remote" {
    organization = "dongsusin-org"
    workspaces {
      name = "MiMiDaily"
    }
  }
}

provider "aws" {
  region = "ap-northeast-2"
}
