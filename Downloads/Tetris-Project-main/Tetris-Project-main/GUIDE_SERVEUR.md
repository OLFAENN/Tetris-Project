# Guide de Configuration du Serveur MySQL

## Étape 1 : Installer MySQL sur votre serveur

### Windows
1. Télécharger MySQL Community Server depuis mysql.com
2. Installer en tant que service Windows
3. Noter le mot de passe root

### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

## Étape 2 : Configurer MySQL pour les connexions distantes

### 1. Modifier la configuration MySQL

**Windows** : Modifier `C:\ProgramData\MySQL\MySQL Server 8.0\my.ini`
**Linux** : Modifier `/etc/mysql/mysql.conf.d/mysqld.cnf`

Changer cette ligne :
```ini
bind-address = 127.0.0.1
```
En :
```ini
bind-address = 0.0.0.0
```

### 2. Redémarrer MySQL
```bash
# Windows
net stop mysql
net start mysql

# Linux
sudo systemctl restart mysql
```

### 3. Exécuter le script de configuration
```bash
mysql -u root -p < setup_mysql_server.sql
```

## Étape 3 : Configurer le pare-feu

### Windows
1. Ouvrir "Pare-feu Windows Defender"
2. "Règles de trafic entrant" → "Nouvelle règle"
3. Port → TCP → 3306 → Autoriser la connexion
4. Appliquer à tous les profils

### Linux
```bash
sudo ufw allow 3306
```

## Étape 4 : Configuration réseau (si nécessaire)

### Si les PC ne sont pas sur le même réseau local :

1. **Trouver votre IP publique** :
   - Aller sur whatismyip.com
   - Noter l'adresse IP

2. **Configurer le port forwarding** sur votre box Internet :
   - Port externe : 3306
   - Port interne : 3306
   - IP interne : L'IP de votre PC serveur

## Étape 5 : Tester la connexion

### Depuis un autre PC :
```bash
mysql -h VOTRE_IP_PUBLIQUE -P 3306 -u tetris_user -p
```

### Depuis votre jeu :
1. Modifier `DatabaseConnection.java`
2. Remplacer `123.45.67.89` par votre IP publique
3. Compiler et tester

## Étape 6 : Sécurité (Recommandé)

### Créer un utilisateur avec des droits limités :
```sql
CREATE USER 'tetris_user'@'%' IDENTIFIED BY 'mot_de_passe_complexe';
GRANT SELECT, INSERT, UPDATE, DELETE ON tetris_db.* TO 'tetris_user'@'%';
FLUSH PRIVILEGES;
```

### Utiliser SSL (optionnel) :
Modifier l'URL dans `DatabaseConnection.java` :
```java
private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + 
                                 "?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
```

## Utilisateurs de test créés :
- **admin** / admin123
- **joueur1** / joueur123  
- **test_user** / test123

## Dépannage

### Erreur "Connection refused"
- Vérifier que MySQL écoute sur 0.0.0.0
- Vérifier le pare-feu
- Vérifier le port forwarding

### Erreur "Access denied"
- Vérifier les privilèges de l'utilisateur
- Vérifier le mot de passe

### Erreur "Host not allowed"
- Vérifier que l'utilisateur a `@'%'` et non `@'localhost'` 