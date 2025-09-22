# provider.tf
provider "aws" {
  region = "ap-northeast-2"
}

# ec2.tf
resource "aws_instance" "ec2" {
  ami           = "ami-0a5a6128e65676ebb" # Amazon Linux 2 (ap-northeast-2)
  instance_type = "t2.micro"
}

# outputs.tf
output "ec2_ip" {
  description = "Private IP of the EC2 instance"
  value       = aws_instance.ec2.private_ip
}