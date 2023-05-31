terraform {
        required_providers {
                aws = {
                        source  = "hashicorp/aws"
                        version = "~> 4.16.0"
                }
        }
}

provider "aws" {
        profile = "default"
        region  = "us-east-1"
}

module "vpc" {
        source  = "terraform-aws-modules/vpc/aws"
        version = "3.14.0"
        name = "E7-EB-PI2-VPC"
        cidr = "10.0.0.0/16"
        azs = ["us-east-1a","us-east-1b"]
        public_subnets = ["10.0.0.0/24","10.0.1.0/24"]
	private_subnets = ["10.0.2.0/24","10.0.3.0/24"]
	enable_nat_gateway = true
	single_nat_gateway = true
	one_nat_gateway_per_az = false
}

module "key-pair" {
  source  = "terraform-aws-modules/key-pair/aws"
  version = "1.0.1"
  key_name = "E7-EB-PI2-key"
  public_key = file("${path.module}/id_rsa.pub")
}

module "sg-public" {
        source  = "terraform-aws-modules/security-group/aws"
        version = "4.9.0"
        name = "E7-EB-PI2-SG_PUBLIC"
        description = "Public security group"
        vpc_id = module.vpc.vpc_id #REVISAR
        egress_rules = ["all-all"]
        ingress_with_cidr_blocks = [
                {
                        from_port = 22
                        to_port = 22
                        protocol = "tcp"
                        description = "SSH service"
                        cidr_blocks = "0.0.0.0/0"
                },
                {
                        from_port = 80
                        to_port = 80
                        protocol = "tcp"
                        description = "HTTP service"
                        cidr_blocks = "0.0.0.0/0" #revisar
                },


        ]

}

module "sg-private" {
        source  = "terraform-aws-modules/security-group/aws"
        version = "4.9.0"
        name = "E7-EB-PI2-SG_PRIVATE"
        description = "Private security group"
        vpc_id = module.vpc.vpc_id #REVISAR
        egress_rules = ["all-all"]
        ingress_with_cidr_blocks = [
                {
                        from_port = 22
                        to_port = 22
                        protocol = "tcp"
                        description = "SSH service"
                        cidr_blocks = "0.0.0.0/0"
                },
                {
                        from_port = 80
                        to_port = 80
                        protocol = "tcp"
                        description = "HTTP service"
                        cidr_blocks = "0.0.0.0/0" #revisar
                },
		{
                        from_port = 8761
                        to_port = 8761
                        protocol = "tcp"
                        description = "HTTP service"
                        cidr_blocks = "0.0.0.0/0" #revisar
                },
		{
                        from_port = 8080
                        to_port = 8080
                        protocol = "tcp"
                        description = "HTTP service"
                        cidr_blocks = "0.0.0.0/0" #revisar
                },
		{
                        from_port = 3306
                        to_port = 3306
                        protocol = "tcp"
                        description = "HTTP service"
                        cidr_blocks = "0.0.0.0/0" #revisar
                },


        ]

}

module "ec2-configuration" {
    source  = "terraform-aws-modules/ec2-instance/aws"
    version = "4.0.0"
    name = "E7-EB-PI2-CONFIGURATION-ec2"
    ami = "ami-007855ac798b5175e"
    instance_type = "t2.micro"
    key_name = module.key-pair.key_pair_key_name  #revisar
    vpc_security_group_ids = [module.sg-public.security_group_id]
    subnet_id = module.vpc.public_subnets[0]
    availability_zone = module.vpc.azs[0]
    associate_public_ip_address = true  # que los demas en internet puedan acceder
}

module "ec2-api_gateway" {
    source  = "terraform-aws-modules/ec2-instance/aws"
    version = "4.0.0"
    name = "E7-EB-PI2-API_GATEWAY-ec2"
    ami = "ami-007855ac798b5175e"
    instance_type = "t2.micro"
    key_name = module.key-pair.key_pair_key_name  #revisar
    vpc_security_group_ids = [module.sg-public.security_group_id]
    subnet_id = module.vpc.public_subnets[1]
    availability_zone = module.vpc.azs[1]
    associate_public_ip_address = true  # que los demas en internet puedan acceder
}

module "ec2-db_digital_money" {
    source  = "terraform-aws-modules/ec2-instance/aws"
    version = "4.0.0"
    name = "E7-EB-PI2-DB-DIGITAL_MONEY-ec2"
    ami = "ami-007855ac798b5175e"
    instance_type = "t2.micro"
    key_name = module.key-pair.key_pair_key_name  #revisar
    vpc_security_group_ids = [module.sg-private.security_group_id]
    subnet_id = module.vpc.private_subnets[0]
    availability_zone = module.vpc.azs[0]
    associate_public_ip_address = false  # que los demas en internet puedan acceder
}

module "ec2-eureka" {
    source  = "terraform-aws-modules/ec2-instance/aws"
    version = "4.0.0"
    name = "E7-EB-PI2-EUREKA-ec2"
    ami = "ami-007855ac798b5175e"
    instance_type = "t2.micro"
    key_name = module.key-pair.key_pair_key_name  #revisar
    vpc_security_group_ids = [module.sg-private.security_group_id]
    subnet_id = module.vpc.private_subnets[1]
    availability_zone = module.vpc.azs[1]
    associate_public_ip_address = false  # que los demas en internet puedan acceder
}

module "ec2-user" {
    source  = "terraform-aws-modules/ec2-instance/aws"
    version = "4.0.0"
    name = "E7-EB-PI2-USER-ec2"
    ami = "ami-007855ac798b5175e"
    instance_type = "t2.micro"
    key_name = module.key-pair.key_pair_key_name  #revisar
    vpc_security_group_ids = [module.sg-private.security_group_id]
    subnet_id = module.vpc.private_subnets[0]
    availability_zone = module.vpc.azs[0]
    associate_public_ip_address = false  # que los demas en internet puedan acceder
}

module "ec2-keycloak" {
    source  = "terraform-aws-modules/ec2-instance/aws"
    version = "4.0.0"
    name = "E7-EB-PI2-KEYCLOAK-ec2"
    ami = "ami-007855ac798b5175e"
    instance_type = "t2.micro"
    key_name = module.key-pair.key_pair_key_name  #revisar
    vpc_security_group_ids = [module.sg-private.security_group_id]
    subnet_id = module.vpc.private_subnets[1]
    availability_zone = module.vpc.azs[1]
    associate_public_ip_address = false  # que los demas en internet puedan acceder
}


output "ec2-configuration-pub-ip" {
  value = module.ec2-configuration.public_ip
}

output "ec2-api_gateway-pub-ip" {
  value = module.ec2-api_gateway.public_ip
}

output "ec2-db_digital_money-pri-ip" {
  value = module.ec2-db_digital_money.private_ip
}

output "ec2-eureka-pri-ip" {
  value = module.ec2-eureka.private_ip
}

output "ec2-user-pri-ip" {
  value = module.ec2-user.private_ip
}

output "ec2-keycloak-pri-ip" {
  value = module.ec2-keycloak.private_ip
}



