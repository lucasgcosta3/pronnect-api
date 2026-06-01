# 🚀 Guia de Deploy - Railway

## 1️⃣ PRÉ-REQUISITOS

- [ ] Conta no [Railway.app](https://railway.app)
- [ ] Repositório Git (GitHub, GitLab, Bitbucket)
- [ ] CLI do Railway instalado (opcional, mas recomendado)

---

## 2️⃣ PASSO A PASSO - SETUP NO RAILWAY

### **A. Conectar Repositório**

1. Acesse [railway.app](https://railway.app) e faça login
2. Clique em **"New Project"**
3. Selecione **"Deploy from GitHub"**
4. Autorize o Railway a acessar seus repositórios
5. Selecione `pronnect-api`
6. Clique em **"Deploy Now"**

---

### **B. Configurar Variáveis de Ambiente**

No dashboard do Railway, vá para **Variables** e configure:

#### **Banco de Dados (PostgreSQL)**

```env
DB_URL=postgresql://${{Postgres.PGUSER}}:${{Postgres.PGPASSWORD}}@${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/pronnect
DB_USER=${{Postgres.PGUSER}}
DB_PASS=${{Postgres.PGPASSWORD}}
```

#### **JWT & Segurança**

```env
JWT_SECRET=SEU_BASE64_SECRET_AQUI
JWT_EXPIRATION=86400000
```

**Como gerar um JWT_SECRET seguro:**

```bash
# Use este comando (no terminal):
openssl rand -base64 32
```

#### **CORS & Aplicação**

```env
CORS_ORIGIN=https://seu-frontend.com
PORT=8080
PAYMENT_MOCK_ENABLED=false
PAYMENT_AUTO_APPROVE_SECONDS=10
```

---

### **C. Adicionar PostgreSQL**

1. No projeto Railway, clique em **"+ Add"**
2. Selecione **"PostgreSQL"**
3. Confirme - o PostgreSQL será criado automaticamente
4. As variáveis `${{Postgres.*}}` ficarão disponíveis

---

### **D. Deploy Automático**

Railway detectará automaticamente:

- ✅ Dockerfile (se existir) - usará ele
- ✅ Maven (pom.xml) - detectará automaticamente
- ✅ Java 21 será usado (via Dockerfile)

O deploy acontecerá:

- 📤 Automaticamente ao fazer `git push` no branch main/master
- 📊 Você pode acompanhar no Dashboard do Railway
- 🔄 Logs em tempo real disponíveis

---

## 3️⃣ VALIDAÇÃO PÓS-DEPLOY

### **Testar Endpoints**

```bash
# Substitua pela URL do seu railway
RAILWAY_URL="https://seu-app.railway.app"

# Health check
curl $RAILWAY_URL/actuator/health

# Listar skills
curl $RAILWAY_URL/api/skills
```

### **Acompanhar Logs**

No dashboard Railway:

- **Deployments** - histórico de deploys
- **Logs** - logs em tempo real
- **Monitoring** - CPU, memória, requisições
- **Variables** - variáveis de ambiente

---

## 4️⃣ TROUBLESHOOTING COMUM

### **Erro: "Cannot connect to database"**

```
✅ Solução:
1. Verifique se PostgreSQL foi adicionado ao projeto
2. Confirme as variáveis DB_URL, DB_USER, DB_PASS
3. Verifique se a migration Flyway rodou (check logs)
```

### **Erro: "Port already in use"**

```
✅ Solução:
Railway redireciona automaticamente para 8080
Suas variáveis de ambiente estão corretas no code
```

### **App inicia mas endpoints retornam 401**

```
✅ Solução:
Verifique JWT_SECRET está configurado
Use um novo token de login
```

### **Migrations não rodaram**

```
✅ Solução:
Check logs: `Flyway validation failed`
Garanta que application-prod.yaml tem:
  flyway:
    enabled: true
    locations: classpath:db/migration
```

---

## 5️⃣ PRIMEIRO DEPLOY - CHECKLIST

- [ ] Dockerfile criado ✅
- [ ] railway.json criado ✅
- [ ] Conta no Railway criada
- [ ] Repositório conectado no Railway
- [ ] PostgreSQL adicionado
- [ ] Variáveis de ambiente configuradas
- [ ] JWT_SECRET gerado e configurado
- [ ] CORS_ORIGIN ajustado para seu frontend
- [ ] Git push realizado
- [ ] Deploy iniciado no Railway
- [ ] Logs verificados (sem erros)
- [ ] Endpoints testados

---

## 6️⃣ DEPLOY CONTÍNUO

Após o primeiro deploy bem-sucedido:

```bash
# Simples assim - qualquer push atualiza automaticamente
git add .
git commit -m "feat: atualizações para deploy"
git push origin main

# Railway detecta, faz build e deploy automaticamente!
```

---

## 7️⃣ MONITORAMENTO & MAINTENANCE

### **Alerts (Configurar no Railway)**

- [ ] Erro de deployment
- [ ] Uso de memória > 90%
- [ ] Latência > 1000ms

### **Backups PostgreSQL**

```
Railway faz backup automático
Você pode fazer restore via dashboard
```

### **Escalar App**

Se precisar mais recursos:

1. Dashboard Railway
2. Variáveis de ambiente
3. Aumente CPU/RAM conforme necessário

---

## 📚 RECURSOS ÚTEIS

- [Documentação Railway](https://docs.railway.app)
- [Railway Nixpacks](https://nixpacks.com)
- [Spring Boot em Railway](https://docs.railway.app/deploy/backends/java)
- [PostgreSQL Railway](https://docs.railway.app/add-ons/postgresql)
