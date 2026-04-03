-- EARN_GOLD.lua
-- KEYS[1] = wallet:gold:{userNo}   (string)
-- KEYS[2] = delta:gold             (hash)

-- ARGV[1] = userNo
-- ARGV[2] = amount

local userNo = ARGV[1]
local amount = tonumber(ARGV[2])

if amount == nil or amount <= 0 then
  return {0, -1}
end

redis.call('INCRBY', KEYS[1], amount)
redis.call('HINCRBY', KEYS[2], userNo, amount)

local bal = tonumber(redis.call('GET', KEYS[1]) or '0')
return {1, bal}
