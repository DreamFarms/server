-- SPEND_GOLD.lua
-- KEYS[1] = wallet:gold:{userNo}   (string)
-- KEYS[2] = delta:gold             (hash)

-- ARGV[1] = userNo
-- ARGV[2] = cost

local userNo = ARGV[1]
local cost = tonumber(ARGV[2])

if cost == nil or cost <= 0 then
  return {0, -1}
end

local bal = tonumber(redis.call('GET', KEYS[1]) or '0')
if bal < cost then
  return {0, bal}
end

redis.call('DECRBY', KEYS[1], cost)
redis.call('HINCRBY', KEYS[2], userNo, -cost)

return {1, bal - cost}
