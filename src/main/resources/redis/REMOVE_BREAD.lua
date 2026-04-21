-- KEYS[1] = display:slot:{userNo}      (hash: "tableNo:slotNo" -> "foodNo,count")
-- KEYS[2] = inv:food:{userNo}          (hash: foodNo -> count)
-- KEYS[3] = delta:display:{userNo}     (hash: "tableNo:slotNo" -> deltaCount)
-- KEYS[4] = delta:inv:food:{userNo}    (hash: foodNo -> deltaCount)

-- ARGV[1] = tableNo
-- ARGV[2] = slotNo
-- ARGV[3] = quantity

local tableNo = ARGV[1]
local slotNo = ARGV[2]
local quantity = tonumber(ARGV[3])

if quantity == nil or quantity <= 0 then
  return {0, -1, 0, 0}
end

local slotField = tableNo .. ":" .. slotNo
local slotValue = redis.call('HGET', KEYS[1], slotField)

if not slotValue then
  return {0, -2, 0, 0} -- 빈 슬롯
end

local commaPos = string.find(slotValue, ",")
if not commaPos then
  return {0, -4, 0, 0}
end

local foodNo = string.sub(slotValue, 1, commaPos - 1)
local curCount = tonumber(string.sub(slotValue, commaPos + 1))

if curCount == nil then
  return {0, -5, 0, 0}
end

if curCount < quantity then
  return {0, -3, curCount, 0}
end

local remainSlotCount = curCount - quantity

if remainSlotCount == 0 then
  redis.call('HDEL', KEYS[1], slotField)
else
  redis.call('HSET', KEYS[1], slotField, foodNo .. "," .. remainSlotCount)
end

redis.call('HINCRBY', KEYS[3], slotField, -quantity)
redis.call('HINCRBY', KEYS[2], foodNo, quantity)
redis.call('HINCRBY', KEYS[4], foodNo, quantity)

local inventoryCount = tonumber(redis.call('HGET', KEYS[2], foodNo) or '0')
return {1, tonumber(foodNo), remainSlotCount, inventoryCount}