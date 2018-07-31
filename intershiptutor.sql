-- MySQL dump 10.13  Distrib 5.7.19, for Win64 (x86_64)
--
-- Host: localhost    Database: intershiptutor
-- ------------------------------------------------------
-- Server version	5.7.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `amministratore`
--

DROP TABLE IF EXISTS `amministratore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `amministratore` (
  `utente` varchar(16) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(15) NOT NULL,
  PRIMARY KEY (`utente`),
  KEY `fk_amministratore_utente1_idx` (`utente`),
  CONSTRAINT `fk_amministratore_utente1` FOREIGN KEY (`utente`) REFERENCES `utente` (`codice_fiscale`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=REDUNDANT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amministratore`
--

LOCK TABLES `amministratore` WRITE;
/*!40000 ALTER TABLE `amministratore` DISABLE KEYS */;
INSERT INTO `amministratore` VALUES ('MRTSFN96H14A488B','Stefano','Martella');
/*!40000 ALTER TABLE `amministratore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `azienda`
--

DROP TABLE IF EXISTS `azienda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `azienda` (
  `utente` varchar(16) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `regione` varchar(15) NOT NULL,
  `indirizzo_sede_legale` varchar(45) NOT NULL,
  `foro_competente` varchar(45) NOT NULL,
  `nome_rappresentante` varchar(45) NOT NULL,
  `cognome_rappresentante` varchar(45) NOT NULL,
  `nome_responsabile` varchar(45) NOT NULL,
  `cognome_responsabile` varchar(45) NOT NULL,
  `convenzionata` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`utente`),
  KEY `fk_azienda_utente1_idx` (`utente`),
  CONSTRAINT `fk_azienda_utente1` FOREIGN KEY (`utente`) REFERENCES `utente` (`codice_fiscale`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `azienda`
--

LOCK TABLES `azienda` WRITE;
/*!40000 ALTER TABLE `azienda` DISABLE KEYS */;
INSERT INTO `azienda` VALUES ('03297040364','Azienda5','Guardiagrele','L\'Aquila, via Vetoio','Guardiagrele','Marco','Bianchi','Antonio','Rossi',1),('03297040365','Azienda4','Pescara','Pescara','Pescara','Mario','Verdi','Pippo','Pippino',1),('03297040366','Tirasa','L\'Aquila','L\'Aquila','L\'Aquila','Pinco','Pallone','Giovanni','Muchacha',1),('03297040367','nExpecto','L\'Aquila','L\'Aquila, via Vetoio','L\'Aquila','Henry','Muccini','Francesco','Tarquini',1),('DE91111111','Azienda3','Pescara','Corso Umberto','Pescara','Mario','Rossi','Marco','Verdi',0),('DE999999999','Xenia S.p.A.','Abruzzo','Guardiagrele','Guardiagrele','Giovanna','Melildeo','Giuseppe','Della Penna',1),('HU12345678','Azienda2','L\'Aquila','L\'Aquila','Via Vetotio','Pinco','Pallino','Vattela','Pesca',1);
/*!40000 ALTER TABLE `azienda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offertatirocinio`
--

DROP TABLE IF EXISTS `offertatirocinio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `offertatirocinio` (
  `id_tirocinio` int(11) NOT NULL AUTO_INCREMENT,
  `azienda` varchar(16) NOT NULL,
  `titolo` varchar(255) NOT NULL,
  `luogo` varchar(45) NOT NULL,
  `obiettivi` varchar(2500) NOT NULL,
  `modalita` varchar(2500) NOT NULL,
  `rimborso` varchar(255) DEFAULT NULL,
  `data_inizio` date NOT NULL,
  `data_fine` date NOT NULL,
  `ora_inizio` time DEFAULT NULL,
  `ora_fine` time DEFAULT NULL,
  `numero_ore` int(11) NOT NULL,
  `visibile` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_tirocinio`,`azienda`),
  KEY `fk_tirocinio_azienda1_idx` (`azienda`),
  CONSTRAINT `fk_tirocinio_azienda1` FOREIGN KEY (`azienda`) REFERENCES `azienda` (`utente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offertatirocinio`
--

LOCK TABLES `offertatirocinio` WRITE;
/*!40000 ALTER TABLE `offertatirocinio` DISABLE KEYS */;
INSERT INTO `offertatirocinio` VALUES (1,'DE999999999','Applicazione Web modulare','L\'Aquila','Il progetto di tesi prevede lo sviluppo di un\'applicazione web modulare per l\'analisi di tracciati dati. In particolare, si dovrà da prima prototipare l\'applicazione base e quindi sviluppare un modulo di analisi di esempio da integrare in quest\'ultima.','Lo sviluppo avrà come punto di partenza un\'architettura già definita e prototipata in Java, comprendente il supporto alla modularità e alla generazione dell\'output della analisi. Gli algoritmi di analisi di base, invece, sono già stati progettati da un team di statistici (a dispsizione del tesista) e dovranno essere sviluppati e incorporati nel nuovo sistema a moduli.','600','2018-05-01','2018-07-26',NULL,NULL,0,1),(2,'HU12345678','Realizzazione piattaforma gaming','L\'Aquila','Realizzazione piattaforma gaming','Java, Mysql',NULL,'2018-07-26','2018-07-26',NULL,NULL,0,0),(3,'HU12345678','Realizzazione applicazione','L\'Aquila','Realizzazione applicazione universitaria','Al tirocinante è richiesta la realizzazione dell\'applicazione dell\'Università degli Studi dell\'Aquila. Le tecnologie da usare saranno concordate con il tirocinante, le competenze richieste sono la conoscenza di Ionic, React Native, Java, Mysql ','500','2018-07-26','2018-07-26',NULL,NULL,0,1),(4,'DE999999999','Sviluppo Java','L\'Aquila','Xenia S.p.A. offre tesi magistrali e tirocini in ambito sviluppo Java','Xenia S.p.A., azienda specializzata in servizi di accommodation alberghiera, hospitality process, knowhow ed expertise nella\r\n                                    travel industry con oltre venticinqe anni di attività, offre a laureandi magistrali in informatica con\r\n                                    buona esperienza di sviluppo delle tesi di secondo livello, da integrare possibilmente con un periodo\r\n                                    iniziale di tirocinio. Il progetto di tesi prevede lo sviluppo di un\'applicazione web modulare per l\'analisi\r\n                                    di tracciati dati. In particolare, si dovrà da prima prototipare l\'applicazione base e quindi sviluppare\r\n                                    un modulo di analisi di esempio da integrare in quest\'ultima.','600','2018-07-01','2018-12-20',NULL,NULL,12,1),(5,'DE91111111','Realizzazione di un sistema per il riconoscimento biometrico per gli Uffizi','L\'Aquila','Realizzazione di un sistema per il riconoscimento biometrico per gli Uffizi','Al tirocinante è richiesta la realizzazione di un sistema di riconoscimento biometrico impronta digitale/volto per il Museo degli Uffizi. Il lavoro sarà svolto in team con gli altri membri dell\'azienda. ',NULL,'2018-07-01','2019-01-22',NULL,NULL,150,1);
/*!40000 ALTER TABLE `offertatirocinio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parereazienda`
--

DROP TABLE IF EXISTS `parereazienda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parereazienda` (
  `studente` varchar(16) NOT NULL,
  `azienda` varchar(45) NOT NULL,
  `parere` varchar(255) NOT NULL,
  `voto` enum('1','2','3','4','5') NOT NULL,
  PRIMARY KEY (`studente`,`azienda`),
  KEY `azienda_idx` (`azienda`),
  CONSTRAINT `azienda` FOREIGN KEY (`azienda`) REFERENCES `azienda` (`utente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `studente` FOREIGN KEY (`studente`) REFERENCES `studente` (`utente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parereazienda`
--

LOCK TABLES `parereazienda` WRITE;
/*!40000 ALTER TABLE `parereazienda` DISABLE KEYS */;
INSERT INTO `parereazienda` VALUES ('CRIRIA95M07G438T','DE999999999','Mi sono trovato bene','4'),('MRLDVD96C19A345D','DE999999999','Un\'azienda davvero bella','1');
/*!40000 ALTER TABLE `parereazienda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studente`
--

DROP TABLE IF EXISTS `studente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `studente` (
  `utente` varchar(16) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(15) NOT NULL,
  `data_nascita` datetime NOT NULL,
  `luogo_nascita` varchar(45) NOT NULL,
  `provincia_nascita` varchar(45) NOT NULL,
  `residenza` varchar(45) NOT NULL,
  `provincia_residenza` varchar(45) NOT NULL,
  `tipo_laurea` varchar(45) NOT NULL,
  `corso_laurea` varchar(45) NOT NULL,
  `handicap` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`utente`),
  KEY `fk_studente_utente1_idx` (`utente`),
  CONSTRAINT `fk_studente_utente1` FOREIGN KEY (`utente`) REFERENCES `utente` (`codice_fiscale`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studente`
--

LOCK TABLES `studente` WRITE;
/*!40000 ALTER TABLE `studente` DISABLE KEYS */;
INSERT INTO `studente` VALUES ('CRIRIA95M07G438T','Iari','Icaro','1995-08-07 00:00:00','Penne','PE','L\'Aquila','AQ','Triennale','Informatica',0),('MRLDVD96C19A345D','Davide','Morelli','1996-03-19 00:00:00','L\'Aquila','AQ','L\'Aquila','AQ','Triennale','Informatica',0);
/*!40000 ALTER TABLE `studente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tirociniostudente`
--

DROP TABLE IF EXISTS `tirociniostudente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tirociniostudente` (
  `studente` varchar(16) NOT NULL,
  `tirocinio` int(11) NOT NULL,
  `cfu` int(11) NOT NULL,
  `tutore_universitario` varchar(45) NOT NULL,
  `telefono_tutore` varchar(45) NOT NULL,
  `email_tutore` varchar(255) NOT NULL,
  `descrizione_dettagliata` varchar(2000) NOT NULL,
  `ore_svolte` int(11) NOT NULL,
  `giudizio_finale` varchar(255) NOT NULL,
  `parere` varchar(2500) NOT NULL,
  `stato` enum('attesa','accettato','rifiutato','terminato') DEFAULT 'attesa',
  PRIMARY KEY (`studente`,`tirocinio`),
  KEY `tirocinio_idx` (`tirocinio`),
  CONSTRAINT `studente1` FOREIGN KEY (`studente`) REFERENCES `studente` (`utente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tirocinio` FOREIGN KEY (`tirocinio`) REFERENCES `offertatirocinio` (`id_tirocinio`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tirociniostudente`
--

LOCK TABLES `tirociniostudente` WRITE;
/*!40000 ALTER TABLE `tirociniostudente` DISABLE KEYS */;
INSERT INTO `tirociniostudente` VALUES ('MRLDVD96C19A345D',1,6,'Giuseppe Della Penna','333222111','giuseppe.dellapenna@univaq.it','Lo studente ha realizzato l\'applicazione mobile per l\'università',0,'Il giudizio finale è positivo','Bella esperienza formativa','accettato');
/*!40000 ALTER TABLE `tirociniostudente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utente` (
  `codice_fiscale` varchar(16) NOT NULL,
  `username` varchar(255) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(256) NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `tipo` enum('studente','azienda','amministratore') NOT NULL,
  PRIMARY KEY (`codice_fiscale`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `telefono_UNIQUE` (`telefono`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES ('03297040364','azienda5','azienda5@gmail.it','azienda5','0888888888','azienda'),('03297040365','azienda4','azienda4@gmail.it','azienda4','0858710104','azienda'),('03297040366','tirasa','tirasa.gmail.it','tirasa','08620862','azienda'),('03297040367','nexpecto','nExpecto@gmail.it','nexpecto','08620863','azienda'),('CRIRIA95M07G438T','iari','iari.icaro@student.univaq.it','iari','3276595215','studente'),('DE91111111','azienda3','azienda3@gmail.it','azienda3','331331331','azienda'),('DE999999999','azienda1','azienda1@gmail.it','azienda1','331234567','azienda'),('HU12345678','azienda2','azienda2@gmail.it','azienda2','334123456','azienda'),('MRLDVD96C19A345D','davide','davide.morelli1@student.univaq.it','davide','3295828697','studente'),('MRTSFN96H14A488B','stefano','stefano.martella@student.univaq.it','stefano','3318625176','amministratore');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-07-31 17:18:54
