provider "aws" {
  region = "ap-northeast-2"
}

resource "aws_instance" "ec2" {
  ami                         = "ami-0a5a6128e65676ebb"
  instance_type               = "t2.micro"
  subnet_id                   = "subnet-063cf26dbb1821305" # 실제 PRJ-VPC의 서브넷 ID로 교체
  associate_public_ip_address = true  # 퍼블릭 IP 자동 할당
}

output "ec2_ip" {
  description = "Private IP of the EC2 instance"
  value       = aws_instance.ec2.private_ip
}