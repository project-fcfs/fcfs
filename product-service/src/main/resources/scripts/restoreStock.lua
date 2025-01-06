for i = 1, #ARGV do
    local productKey = KEYS[i]
    local cleanOrderCount = string.match(ARGV[i], "%d+")
    local orderCount = tonumber(cleanOrderCount)

    local quantity = redis.call('HGET', productKey, 'quantity')
    local cleanQuantityNumber = string.match(quantity, "%d+")
    local quantityNumber = tonumber(cleanQuantityNumber)

    local reamingNumber = quantityNumber + orderCount

    -- quantity를 증가
    redis.call('HSET', productKey, 'quantity', reamingNumber)
end

return true
