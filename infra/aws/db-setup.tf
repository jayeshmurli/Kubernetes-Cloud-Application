resource "aws_db_subnet_group" "webapp_db_subnet_group" {
  name       = "webapp-db-subnet"
  subnet_ids = "${var.public_subnets}"

  tags = {
    Name = "webapp_db_subnet"
  }
}

resource "aws_db_instance" "webapp_db" {
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "mysql"
  engine_version       = "5.7"
  instance_class       = "db.t2.micro"
  name                 = "recipe"
  username             = "admin"
  password             = "password"
  identifier           = "recipedb"
  db_subnet_group_name = "${aws_db_subnet_group.webapp_db_subnet_group.id}"
  publicly_accessible  = true
  skip_final_snapshot  = true
  vpc_security_group_ids = ["${var.db_sg}"]
}