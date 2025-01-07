for i = 1, #ARGV do
    local productKey = KEYS[i]
    local cleanOrderCount = string.match(ARGV[i], "%d+")

    local orderCount = tonumber(cleanOrderCount)

    local quantity = redis.call('HGET', productKey, 'quantity')

    local cleanQuantityNumber = string.match(quantity, "%d+")

    local quantityNumber = tonumber(cleanQuantityNumber)
    if not quantityNumber or quantityNumber - orderCount < 0 then
        return false
    end
end

-- 모든 제품에 대해 quantity를 차감
for i = 1, #ARGV do
    local productKey = KEYS[i]
    local cleanOrderCount = string.match(ARGV[i], "%d+")
    local orderCount = tonumber(cleanOrderCount)

    local quantity = redis.call('HGET', productKey, 'quantity')
    local cleanQuantityNumber = string.match(quantity, "%d+")
    local quantityNumber = tonumber(cleanQuantityNumber)

    local reamingNumber = quantityNumber - orderCount

    -- quantity를 차감
    redis.call('HSET', productKey, 'quantity', reamingNumber)
end

return true