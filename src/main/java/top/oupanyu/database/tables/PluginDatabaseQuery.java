package top.oupanyu.database.tables;

public class PluginDatabaseQuery {
    public static String mysql = "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\n" +
            "/*!40101 SET NAMES utf8 */;\n" +
            "/*!50503 SET NAMES utf8mb4 */;\n" +
            "/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;\n" +
            "/*!40103 SET TIME_ZONE='+00:00' */;\n" +
            "/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\n" +
            "/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\n" +
            "/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;\n" +
            "\n" +
            "CREATE DATABASE IF NOT EXISTS `lotsmp` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci */;\n" +
            "USE `lotsmp`;\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS `rhythm` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `songname` text NOT NULL,\n" +
            "  `enable` bit(1) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;\n" +
            "\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS `vocaloidsongs` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `quetion` text NOT NULL,\n" +
            "  `answer` text NOT NULL,\n" +
            "  `content2Chinese` text NOT NULL,\n" +
            "  `available` bit(1) NOT NULL DEFAULT b'0',\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;\n" +
            "\n" +
            "\n" +
            "/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;\n" +
            "/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;\n" +
            "/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;\n" +
            "/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n" +
            "/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;\n";


    public static String sqlite = "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\n" +
            "/*!40101 SET NAMES utf8 */;\n" +
            "/*!50503 SET NAMES utf8mb4 */;\n" +
            "/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;\n" +
            "/*!40103 SET TIME_ZONE='+00:00' */;\n" +
            "/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\n" +
            "/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\n" +
            "/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS `rhythm` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `songname` text NOT NULL,\n" +
            "  `enable` bit(1) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;\n" +
            "\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS `vocaloidsongs` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `quetion` text NOT NULL,\n" +
            "  `answer` text NOT NULL,\n" +
            "  `content2Chinese` text NOT NULL,\n" +
            "  `available` bit(1) NOT NULL DEFAULT b'0',\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;\n" +
            "\n" +
            "\n" +
            "/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;\n" +
            "/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;\n" +
            "/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;\n" +
            "/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n" +
            "/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;\n";

    public static void release(){
        mysql = null;
    }
}
