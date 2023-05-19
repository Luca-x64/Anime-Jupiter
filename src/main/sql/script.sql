drop database anime_jupyter;
create database anime_jupyter;
use anime_jupyter;
-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mag 19, 2023 alle 23:59
-- Versione del server: 10.4.28-MariaDB
-- Versione PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `anime_jupyter`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `anime`
--

CREATE TABLE `anime` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `publisher` varchar(255) NOT NULL,
  `plot` text NOT NULL,
  `link` varchar(255) NOT NULL,
  `imagePath` varchar(255) NOT NULL,
  `episodes` int(11) NOT NULL,
  `year` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `anime`
--

INSERT INTO `anime` (`id`, `title`, `author`, `publisher`, `plot`, `link`, `imagePath`, `episodes`, `year`) VALUES
(1, 'Bleach', 'Kite Kubo', 'Studio Pierrot', 'Ichigo Kurosaki is a young man gifted with the ability to see spirits. His life undergoes a drastic change when a shinigami named Rukia Kuchiki crosses his path in search of a Hollow. During the fight with the spirit Rukia she is seriously injured and is forced to transfer part of her powers to Ichigo, who accepts the shinigami\'s proposal in an attempt to protect her family. However, during the transfer process, something goes wrong and Ichigo absorbs all of Rukia\'s powers, becoming a full-fledged shinigami. Having lost her powers, Rukia remains stuck in the world of the living until she regains her strength. Meanwhile, Ichigo replaces Rukia in her duties as Shinigami, battling the Hollows and leading the souls to the realm of the afterlife, known as the Soul Society. Over time, Rukia\'s superiors learn of her transfer of powers, which is considered illegal in the Soul Society, and sentence the shinigami to death. Although he is unable to prevent Rukia from being brought back to the Soul Society, Ichigo is preparing to save the girl, thanks to the help of his classmates.', 'https://www.youtube.com/watch?v=AFPYnWV1Y-M', 'src/main/resources/img/data/bleach.JPG', 366, 2004),
(2, 'Baki', 'Keisuke Itagaki', 'TMS Entertainment', 'Baki Hanma is a carefree student with a rather unusual hobby: fighting. More precisely, he likes to duel in secret martial arts tournaments that bring together the strongest fighters in the world, who compete in terrible and bloody fights. New Grappler Baki begins after the first series ends. After winning the illegal martial arts tournament, Baki returns to his quiet school life. But a strange phenomenon is occurring, in fact, news reaches him of the escape, from their respective maximum security prisons, of five bloodthirsty martial arts practitioners sentenced to death. They appear to come from different backgrounds, with no apparent connection between them, and are now headed for Japan. Only one thing unites them, they all left the same message: \"I want to know the defeat, appointment in Tokyo\"', 'https://www.youtube.com/watch?v=1fSp8NJEw34', 'src/main/resources/img/data/baki.JPG', 39, 2018),
(3, 'Demon Slayer', 'Koyoharu Gotouge', 'ufotable', 'Tanjiro is the eldest son of a family that has lost their father. One day, he visits another city to sell coal but ends up spending the night there instead of returning home, due to a rumor about a demon wandering around a nearby mountain at night. When he returns home the following day, there will be a tragedy waiting for him.', 'https://www.youtube.com/watch?v=0R70AqEix5o', 'src/main/resources/img/data/demon-slayer.JPG', 44, 2019),
(4, 'Jujutsu Kaisen', 'Gege Akutami', 'MAPPA', 'Yuuji is a genius in running, but he has no interest in running in circles on a track. He is delighted to be part of the occult research club, but he too is part of it just for fun things get serious when a true spirit shows up at school. Life is going to get really weird at Sugiwasa Town High School!', 'https://www.youtube.com/watch?v=pkKu9hLT-t8', 'src/main/resources/img/data/jujutsu-kaisen.JPG', 24, 2020),
(5, 'Seven Deadly Sins', 'A-1 Pictures', 'Nakaba Suzuki', 'The Seven Deadly Sins, a group of evil knights who attempted to overthrow the kingdom of Britain, were all declared dead, but some claim they are still alive. Ten years later, the Paladins, chosen warriors of the kingdom, orchestrate a coup and overthrow the king, becoming the new rulers. Elizabeth, the king\'s only daughter, sets out on a journey in search of the Seven Deadly Sins, with the intention of recruiting them and regaining her kingdom.', 'https://www.youtube.com/watch?v=jzEun32qXwM', 'src/main/resources/img/data/nanatzu-no-taizai.JPG', 108, 2014),
(6, 'One piece', 'Eiichirō Oda', 'Toei Animation', 'Monkey D. Luffy is a young dreamer pirate who as a child has inadvertently eaten the fruit of the devil Gom Gom which makes him \"elastic\", allowing him to stretch and deform at will, at the expense, however, of the ability to swim. The goal that pushed him into the sea is the ambitious one of becoming the Pirate King. He will therefore have to find the legendary \"One Piece\", the magnificent treasure left by the mythical pirate Gol D. Roger probably on the island of Raftel, at the end of the Major Route, never found and every pirate\'s dream. In his adventure, Luffy will reunite around him a crew and will find himself in the midst of bizarre and extravagant situations, at least as much as the characters, friends or enemies, present in the universe that surrounds him, who often reach absurd and grotesque levels and which give the work a surreal and fun atmosphere.', 'https://www.youtube.com/watch?v=bs79t8yJqpg', 'src/main/resources/img/data/one-piece.JPG', 1015, 1999),
(7, 'SK8 the Infinity', ' ABC', 'BONES', 'The story follows the adventures of Reki, a second-year high school student who loves skateboarding, who is suddenly drawn into the \"S\", a dangerous, no-rules underground competition held in an abandoned mine. Ranga, who has returned to Japan from Canada and has never skated before, gets involved in the competition alongside Reki. Unfair participants, artificial intelligences and other truly unique individuals will compete in this \"youth skate battle\".', 'https://www.youtube.com/watch?v=eUHMO2VMolU', 'src/main/resources/img/data/sk8.JPG', 12, 2021),
(8, 'Tokyo Ghoul', 'Sui Ishida', 'Studio Pierrot', 'Strange and violent murders are taking place in the city of Tokyo. According to police investigations, the deaths are the result of attacks, caused by mysterious \"devouring\" creatures, called Ghouls. The protagonist, Kaneki, a normal university student and his friend Hide, elaborate a theory: those monstrous beings mingle with humans and it is for this reason that no one has ever seen one. Their theory is perhaps not that far from reality. Kaneki will discover it on his own skin, after meeting the charming but voracious Rizesrc.', 'https://www.youtube.com/watch?v=6eLsh_iXTMg', 'src/main/resources/img/data/tokyo-ghoul.JPG', 48, 2014),
(9, 'Vanitas no Carte', 'Jun Mochizuki', 'BONES', 'There once lived a vampire known as Vanitas, hated by his own race for being born under a full blue moon, while most of them are born on a red moon night. Alone and scared, he created \"The Book of Vanitas\", a cursed grimoire that would one day take revenge on all vampires. The manga follows the story of Noah, a young man who in the nineteenth century in Paris, travels on an airship with only one purpose in mind: to find The Book of Vanitas. A vampire\'s sudden attack leads him to meet the mysterious Vanitas, a doctor who specializes in vampires and, much to Noah\'s surprise, an ordinary human being. The man inherited both the name and the Vanitas book of legend and uses the grimoire to heal his patients. Behind this kind behavior, however, there is something sinister.', 'https://www.youtube.com/watch?v=-Ut2JfUCmlM', 'src/main/resources/img/data/vanitas-no-carte.JPG', 24, 2021),
(10, 'Black Clover', 'Yūki Tabata', 'Xebec', 'In a world where magic is at the center of everyday life, Asta was born, a boy completely devoid of any magical ability. His friend is Yuno, on the contrary endowed with great magical strength; the two have been rivals from an early age and have decided to compete for the title of Sorcerer Emperor, the highest magical charge in their kingdom.', 'https://www.youtube.com/watch?v=uUX2Vr6Zc1g', 'src/main/resources/img/data/black-clover.JPG', 170, 2017),
(11, 'The Rising of the Shield Hero', 'Aneko Yusagi', 'Kinema Citrus', 'Naofumi Iwatani is an otaku who spends all his time in games and comics, at least until he suddenly finds himself summoned into a parallel universe. He then discovers that he is one of the four heroes in possession of the legendary weapons, who have the task of saving the world from the destruction of the prophecy. Naofumi is \"The hero of the shield\", the weakest, and therefore finds himself alone and betrayed, with only the company of his shield. Now Naofumi must rise again, become a true hero and save the world.', 'https://www.youtube.com/watch?v=rKnyi3TRznA', 'src/main/resources/img/data/shield-hero.JPG', 38, 2019),
(12, 'Darling in the FranXX', 'Mato', 'Trigger e A-1 Pictures', 'The story is set in the distant future, where the Earth is now devastated and humanity has settled in the mobile walled city Plantation. The pilots \"produced\" in Plantation live in Mistilteinn, also known as a \"bird cage\". Children and teenagers live there unaware of the existence of an external world and of the freedom and breadth of the heavens. Their life has the sole purpose of carrying out the assigned missions, that is to fight against the mysterious giant life forms, called Kyoryu, piloting the Franxx robots. Driving a Franxx is to exist. A boy named Hiro, Code: 016, was once thought of as a prodigy, however, over time, he has fallen behind his peers, so much so that his existence is now deemed unnecessary. He no longer pilots a Franxx and that is tantamount to ceasing to exist. One day a mysterious girl, \"Zero Two\", with two horns sticking out of her head, appears before Hiro?', 'https://www.youtube.com/watch?v=wE5ch0dxbE0', 'src/main/resources/img/data/darling-in-the-franxx.jpg', 24, 2018),
(13, 'Orient', 'Shinobu Ōtaka', 'A.C.G.T.', 'The series takes hold in the Sengoku era, the so-called period of the warring states. Japan is dominated by demons but a 15-year-old named Musashi faces them with the help of a particular power ...', 'https://www.youtube.com/watch?v=J1eLcAExERA', 'src/main/resources/img/data/orient.jpg', 12, 2022),
(14, 'Spy x Family', 'Tatsuya Endo', 'Wit Studio e CloverWorks', 'Twilight, one of the best spies in the world, has spent her life taking on undercover missions to make the world a better place. But one day she receives a particularly difficult task, to succeed in her new mission she will have to form a temporary family and start a new life!', 'https://youtu.be/ofXigq9aIpo', 'src/main/resources/img/data/spy-x-family.png', 12, 2022),
(15, 'The God of High School', 'Yongje Park', 'MAPPA', 'As an island disappears from the face of the earth, a mysterious organization organizes a tournament involving the most skilled wrestlers in the world. Whoever wins will get whatever they ask for as a prize. The organization recruits only the best fighters who will aspire to the title of \"God of High School\"!', 'https://youtu.be/oqjwUfprNAk', 'src/main/resources/img/data/the-god-of-high-school.jpg', 13, 2020),
(16, 'Chuunibyou demo Koi ga Shitai!', 'Torako', 'Kyoto Animation', 'Togashi Youta is a boy who during middle school had \"adolescent manias\" (ch\'niby, or \"seventh grade syndrome\", so he believed he had magical powers and that his real name was \"Dark Flame Master\" alienating himself from reality and his classmates. Now that he has started high school in a school where no one knows him he wants to leave everything behind, however a girl with even worse obsessions than hers named Rikka Takanashi discovers her past and is interested in he.', 'https://www.youtube.com/watch?v=USgrD2Dqsa0', 'src/main/resources/img/data/chuunibyou-demo-koi-ga-shitai!.png', 48, 2012),
(17, 'Scissor Seven', 'Xiaofeng He', 'Sharefun Studio, AHA Entertainment, Nurostar', 'On a small island, Seven, a killer who can disguise himself as anything, carries out low-cost murders by charging only 567 yuan per mission.', 'https://youtu.be/oua5_PcH_N8', 'src/main/resources/img/data/scissor-seven.png', 34, 2018),
(18, 'Kimi to Boku no Saigo no Senjou, Aruiwa Sekai ga Hajimaru Seisen', 'Kei Sazane', 'Silver Link', 'War rages between the scientifically advanced Empire and Nebulis, known as the \"Witch Country\". In this climate of tension, two boys meet: Aliceliese, princess of the witches and Iska, the youngest knight ever to be awarded the title of strongest in the Empire. Iska is captivated by the beauty and nobility of soul of the rival princess, who in turn is struck by the knight\'s strength and lifestyle. But as enemies, the two should fight to the death. Will they be able to escape their fate?', 'https://youtu.be/MvK7MmCfFTA', 'src/main/resources/img/data/kimi-to-boku-no-saigo-no-senjou,-aruiwa-sekai-ga-hajimaru-seisen.png', 12, 2020);

-- --------------------------------------------------------

--
-- Struttura della tabella `favourite`
--

CREATE TABLE `favourite` (
  `user_id` bigint(20) NOT NULL,
  `anime_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `users`
--

CREATE TABLE `users` (
  `id` bigint(11) NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` char(60) DEFAULT NULL,
  `isAdmin` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `users`
--

INSERT INTO `users` (`id`, `nome`, `email`, `password`, `isAdmin`) VALUES
(0, 'admin', 'admin@gmail.com', '$2a$10$2V5kA2rwdiXljcD2sgHULunHA3Jw.Xt2nWrs4q4QsvDgG2/6SSI8S', 1),
(1, 'luca', 'luca@gmail.com', '$2a$10$P9A53deA/60emkyJfz4oSu4vfjm2jSwu9726J0qtYkgqICQnezwEO', 0),
(2, 'manuel', 'manuel@gmail.com', '$2a$10$u.9xCGcg..HqEVcwDjaVI.mz8HAvQmqAXB9qU3IwH4QqeUVF/SbFe', 0),
(3, 'testUser', 'test@test.com', '$2a$10$MIWwROwEssANkJsHyn1PrOYUQiTM.rgHwzMdbQ5uHm9iSIIv4TZDy', 0),
(4, 'test2', 'test2@gmail.com', '$2a$10$p3OiokXkxbRtYiVXhvxqCuFC.7pHfvN1yYxpFeCIAZBZrsz.qLMq.', 0);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `anime`
--
ALTER TABLE `anime`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `favourite`
--
ALTER TABLE `favourite`
  ADD PRIMARY KEY (`user_id`,`anime_id`),
  ADD KEY `anime_id` (`anime_id`);

--
-- Indici per le tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `anime`
--
ALTER TABLE `anime`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT per la tabella `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `favourite`
--
ALTER TABLE `favourite`
  ADD CONSTRAINT `favourite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `favourite_ibfk_2` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
