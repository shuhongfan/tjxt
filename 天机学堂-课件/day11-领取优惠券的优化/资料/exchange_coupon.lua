if(redis.call('GETBIT', KEYS[1], ARGV[1]) == 1) then
    return "1"
end
local arr = redis.call('ZRANGEBYSCORE', KEYS[2], ARGV[1], ARGV[2], 'LIMIT', 0, 1);
if(#arr == 0) then
    return "2"
end
local cid = arr[1]
local _k1 = "prs:coupon:" .. cid
local _k2 = "prs:user:coupon:" .. cid
if(redis.call('EXISTS', _k1) == 0) then
    return "3"
end
if(tonumber(redis.call('time')[1]) > tonumber(redis.call('HGET', _k1, 'issueEndTime'))) then
    return "4"
end
if(tonumber(redis.call('HGET', _k1, 'userLimit')) < redis.call('HINCRBY', _k2, ARGV[3], 1)) then
    return "5"
end
redis.call('SETBIT', KEYS[1], ARGV[1], "1")
return cid