-- SELL_BREAD.lua
-- KEYS[1] = inv:food:{userNo}          (hash: foodNo -> count)
-- KEYS[2] = wallet:gold:{userNo}       (string)
-- KEYS[3] = delta:gold                 (hash: userNo -> deltaGold)
-- KEYS[4] = delta:inv:food:{userNo}    (hash: foodNo -> deltaCount)

-- ARGV[1] = userNo
-- ARGV[2] = foodNo
-- ARGV[3] = qty
-- ARGV[4] = goldInc

local userNo = ARGV[1]
local foodNo = ARGV[2]
local qty = tonumber(ARGV[3])
local goldInc = tonumber(ARGV[4])

if qty == nil or qty <= 0 then
  return {0, -1, 0}
end

if goldInc == nil or goldInc < 0 then
  return {0, -2, 0}
end

local cur = tonumber(redis.call('HGET', KEYS[1], foodNo) or '0')
if cur < qty then
  return {0, cur, 0}
end

-- 즉시 반영 (권위 데이터)
redis.call('HINCRBY', KEYS[1], foodNo, -qty)
redis.call('INCRBY', KEYS[2], goldInc)

-- MySQL 정산용 델타 누적
redis.call('HINCRBY', KEYS[3], userNo, goldInc)
redis.call('HINCRBY', KEYS[4], foodNo, -qty)

local newBal = tonumber(redis.call('GET', KEYS[2]) or '0')
return {1, cur - qty, newBal}
