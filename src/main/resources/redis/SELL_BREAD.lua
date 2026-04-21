-- KEYS[1] = display:slot:{userNo}      (hash: "tableNo:slotNo" -> "foodNo,count")
-- KEYS[2] = wallet:gold:{userNo}       (string)
-- KEYS[3] = delta:gold                 (hash: userNo -> deltaGold)
-- KEYS[4] = delta:display:{userNo}     (hash: "tableNo:slotNo" -> deltaCount)

-- ARGV[1] = userNo
-- ARGV[2] = tableNo
-- ARGV[3] = slotNo
-- ARGV[4] = expectedFoodNo
-- ARGV[5] = quantity
-- ARGV[6] = goldInc

local userNo = ARGV[1]
local tableNo = ARGV[2]
local slotNo = ARGV[3]
local expectedFoodNo = ARGV[4]
local quantity = tonumber(ARGV[5])
local goldInc = tonumber(ARGV[6])

if quantity == nil or quantity <= 0 then
  return {0, -1, 0, 0}
end

if goldInc == nil or goldInc < 0 then
  return {0, -2, 0, 0}
end

local slotField = tableNo .. ":" .. slotNo
local slotValue = redis.call('HGET', KEYS[1], slotField)

if not slotValue then
  return {0, -3, 0, 0} -- 슬롯 비어있음
end

local commaPos = string.find(slotValue, ",")
if not commaPos then
  return {0, -4, 0, 0}
end

local foodNo = string.sub(slotValue, 1, commaPos - 1)
local curCount = tonumber(string.sub(slotValue, commaPos + 1))

if foodNo ~= expectedFoodNo then
  return {0, -6, 0, tonumber(foodNo)} -- 자바에서 읽은 foodNo와 달라짐
end

if curCount == nil then
  return {0, -5, 0, 0}
end

if curCount < quantity then
  return {0, curCount, 0, tonumber(foodNo)}
end

local remainCount = curCount - quantity

if remainCount == 0 then
  redis.call('HDEL', KEYS[1], slotField)
else
  redis.call('HSET', KEYS[1], slotField, foodNo .. "," .. remainCount)
end

redis.call('INCRBY', KEYS[2], goldInc)
redis.call('HINCRBY', KEYS[3], userNo, goldInc)
redis.call('HINCRBY', KEYS[4], slotField, -quantity)

local newBal = tonumber(redis.call('GET', KEYS[2]) or '0')
return {1, remainCount, newBal, tonumber(foodNo)}