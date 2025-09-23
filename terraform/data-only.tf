data "aws_vpcs" "all" {}

# 출력
output "vpc_ids" {
  value = data.aws_vpcs.all.ids
}
