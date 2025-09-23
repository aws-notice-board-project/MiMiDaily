
provider "aws" {
  region = "ap-northeast-2" # 서울 리전
}

# ---------------------
# VPC
# ---------------------
resource "aws_vpc" "prj_vpc" {
  cidr_block           = "10.250.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Name = "PRJ-VPC"
  }
}

# ---------------------
# Subnets
# ---------------------
resource "aws_subnet" "was_pri_2a" {
  vpc_id                  = aws_vpc.prj_vpc.id
  cidr_block              = "10.250.2.0/24"
  availability_zone       = "ap-northeast-2a"
  map_public_ip_on_launch = false

  tags = {
    Name = "PRJ-WAS-PRI-Subnet-2A"
  }
}

resource "aws_subnet" "was_pri_2c" {
  vpc_id                  = aws_vpc.prj_vpc.id
  cidr_block              = "10.250.14.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = false

  tags = {
    Name = "PRJ-WAS-PRI-Subnet-2C"
  }
}

resource "aws_subnet" "was_pub_2c" {
  vpc_id                  = aws_vpc.prj_vpc.id
  cidr_block              = "10.250.12.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = true

  tags = {
    Name = "PRJ-WAS-PUB-Subnet-2C"
  }
}

resource "aws_subnet" "web_pri_2c" {
  vpc_id                  = aws_vpc.prj_vpc.id
  cidr_block              = "10.250.4.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = false

  tags = {
    Name = "PRJ-WEB-PRI-Subnet-2C"
  }
}

resource "aws_subnet" "web_pub_2c" {
  vpc_id                  = aws_vpc.prj_vpc.id
  cidr_block              = "10.250.10.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = true

  tags = {
    Name = "PRJ-WEB-PUB-Subnet-2C"
  }
}

resource "aws_subnet" "db_pri_2a" {
  vpc_id                  = aws_vpc.prj_vpc.id
  cidr_block              = "10.250.3.0/24"
  availability_zone       = "ap-northeast-2a"
  map_public_ip_on_launch = false

  tags = {
    Name = "PRJ-DB-PRI-Subnet-2A"
  }
}

resource "aws_subnet" "db_pri_2c" {
  vpc_id                  = aws_vpc.prj_vpc.id
  cidr_block              = "10.250.1.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = false

  tags = {
    Name = "PRJ-DB-PRI-Subnet-2C"
  }
}

resource "aws_subnet" "db_pri_2c_alt" {
  vpc_id                  = aws_vpc.prj_vpc.id
  cidr_block              = "10.250.13.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = false

  tags = {
    Name = "PRJ-DB-PRI-Subnet-2C"
  }
}

# ---------------------
# Internet Gateway + Route Table
# ---------------------
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.prj_vpc.id

  tags = {
    Name = "PRJ-IGW"
  }
}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.prj_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "PRJ-Public-RT"
  }
}

# Public Subnet Association
resource "aws_route_table_association" "was_pub" {
  subnet_id      = aws_subnet.was_pub_2c.id
  route_table_id = aws_route_table.public.id
}

resource "aws_route_table_association" "web_pub" {
  subnet_id      = aws_subnet.web_pub_2c.id
  route_table_id = aws_route_table.public.id
}

# ---------------------
# Security Group (예: Bastion Host용)
# ---------------------
resource "aws_security_group" "bastion_sg" {
  vpc_id = aws_vpc.prj_vpc.id
  name   = "bastion-sg"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "bastion-sg"
  }
}

# ---------------------
# EC2 Bastion Host
# ---------------------
resource "aws_instance" "bastion" {
  ami           = "ami-0c9c942bd7bf113a2" # Amazon Linux 2 (서울 리전 예시)
  instance_type = "t3.medium"
  subnet_id     = aws_subnet.was_pub_2c.id
  vpc_security_group_ids = [aws_security_group.bastion_sg.id]

  tags = {
    Name = "PRJ-BASTION-2A"
  }
}
