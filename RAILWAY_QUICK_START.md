# ⚡ QUICK START - Railway Deploy

## Em 5 Minutos

### 1. Gere um JWT Secret

```bash
openssl rand -base64 32
# Salve o resultado acima - você vai precisar
```

### 2. Acesse railway.app

- Login → New Project → Deploy from GitHub
- Selecione `pronnect-api`

### 3. Clique "+ Add" → PostgreSQL

- Railway vai criar o banco automaticamente

### 4. Configure as 6 Variáveis Essenciais

Na aba **Variables** do Railway, adicione:

| Variável                 | Valor                                                                                                           |
| ------------------------ | --------------------------------------------------------------------------------------------------------------- |
| `JWT_SECRET`             | Cole o resultado do openssl acima                                                                               |
| `CORS_ORIGIN`            | `https://seu-frontend.railway.app`                                                                              |
| `DB_URL`                 | `postgresql://${{Postgres.PGUSER}}:${{Postgres.PGPASSWORD}}@${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/pronnect` |
| `DB_USER`                | `${{Postgres.PGUSER}}`                                                                                          |
| `DB_PASS`                | `${{Postgres.PGPASSWORD}}`                                                                                      |
| `SPRING_PROFILES_ACTIVE` | `prod`                                                                                                          |

### 5. Deploy Automático

```bash
git add .
git commit -m "prep: setup railway deployment"
git push origin main
```

Railway detecta automaticamente e faz build + deploy! 🚀

---

## Status do Deploy

No dashboard Railway você vê:

- 🔴 Building... → ⏳ Waiting... → 🟢 Running

Clique em **"Logs"** para ver output em tempo real.

---

## Testar

```bash
# Substitua pela sua URL Railway
RAIL_URL="https://seu-app.railway.app"

# Health Check
curl $RAIL_URL/actuator/health

# List Skills
curl $RAIL_URL/api/skills
```

---

## Troubleshoot

❌ **502 Bad Gateway** → Verifique logs no Railway  
❌ **Cannot find database** → Verifique se PostgreSQL foi adicionado  
❌ **401 Unauthorized** → Configure JWT_SECRET correto

Mais detalhes em: [RAILWAY_DEPLOY.md](./RAILWAY_DEPLOY.md)
