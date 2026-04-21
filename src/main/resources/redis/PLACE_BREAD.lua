-- KEYS[1] = inv:food:{userNo}          (hash: foodNo -> count)
-- KEYS[2] = display:slot:{userNo}      (hash: "tableNo:slotNo" -> "foodNo,count")
-- KEYS[3] = delta:inv:food:{userNo}    (hash: foodNo -> deltaCount)
-- KEYS[4] = delta:display:{userNo}     (hash: "tableNo:slotNo" -> deltaCount)

-- ARGV[1] = tableNo
-- ARGV[2] = slotNo
-- ARGV[3] = foodNo
-- ARGV[4] = quantity

local tableNo = ARGV[1]
local slotNo = ARGV[2]
local foodNo = ARGV[3]
local quantity = tonumber(ARGV[4])

if quantity == nil or quantity <= 0 then
  return {0, -1, 0}
end

local invCount = tonumber(redis.call('HGET', KEYS[1], foodNo) or '0')
if invCount < quantity then
  return {0, -2, invCount}
end

local slotField = tableNo .. ":" .. slotNo
local slotValue = redis.call('HGET', KEYS[2], slotField)

local newSlotCount = quantity

if slotValue then
  local commaPos = string.find(slotValue, ",")
  if not commaPos then
    return {0, -4, 0}
  end

  local existingFoodNo = string.sub(slotValue, 1, commaPos - 1)
  local existingCount = tonumber(string.sub(slotValue, commaPos + 1))

  if existingFoodNo ~= foodNo then
    return {0, -3, existingCount or 0} -- 다른 빵 존재
  end

  newSlotCount = existingCount + quantity
end

redis.call('HINCRBY', KEYS[1], foodNo, -quantity)
redis.call('HINCRBY', KEYS[3], foodNo, -quantity)

redis.call('HSET', KEYS[2], slotField, foodNo .. "," .. newSlotCount)
redis.call('HINCRBY', KEYS[4], slotField, quantity)

local remainInv = tonumber(redis.call('HGET', KEYS[1], foodNo) or '0')
return {1, remainInv, newSlotCount}