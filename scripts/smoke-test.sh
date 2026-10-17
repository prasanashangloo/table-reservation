#!/usr/bin/env bash
# Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
# End-to-end API smoke test against a running instance.
# Usage: scripts/smoke-test.sh [base_url]     e.g. http://localhost:8080  or http://localhost:8080/reservation
set -euo pipefail
BASE="${1:-http://localhost:8080}"
EMAIL="smoke-$(date +%s)@test.com"
DATE="$(date -d '+3 days' +%F 2>/dev/null || date -v+3d +%F)"
json() { python3 -c "import sys,json; print(json.load(sys.stdin)['$1'])"; }

echo "1. health";   curl -sf "$BASE/api/health"; echo
echo "2. register"; curl -sf -X POST "$BASE/api/users/register" -H 'Content-Type: application/json' \
  -d "{\"name\":\"Smoke\",\"email\":\"$EMAIL\",\"password\":\"secret123\"}"; echo
TOKEN=$(curl -sf -X POST "$BASE/api/users/login" -H 'Content-Type: application/json' \
  -d "{\"email\":\"$EMAIL\",\"password\":\"secret123\"}" | json token)
echo "3. logged in (token ${TOKEN:0:8}...)"
echo "4. availability"; curl -sf "$BASE/api/availability?date=$DATE&partySize=2" | head -c 200; echo
ID=$(curl -sf -X POST "$BASE/api/bookings" -H "X-Auth-Token: $TOKEN" -H 'Content-Type: application/json' \
  -d "{\"tableId\":1,\"date\":\"$DATE\",\"slot\":\"20:00\",\"partySize\":2}" | json id)
echo "5. booked #$ID (PENDING)"
CODE=$(curl -s -o /dev/null -w '%{http_code}' -X POST "$BASE/api/bookings" -H "X-Auth-Token: $TOKEN" \
  -H 'Content-Type: application/json' -d "{\"tableId\":1,\"date\":\"$DATE\",\"slot\":\"20:00\",\"partySize\":2}")
[ "$CODE" = "409" ] && echo "6. double booking rejected (409)" || { echo "FAIL expected 409 got $CODE"; exit 1; }
curl -sf -X POST "$BASE/api/bookings/$ID/confirm" -H "X-Auth-Token: $TOKEN" | json status
curl -sf -X POST "$BASE/api/bookings/$ID/cancel"  -H "X-Auth-Token: $TOKEN" | json status
echo "SMOKE TEST PASSED"
