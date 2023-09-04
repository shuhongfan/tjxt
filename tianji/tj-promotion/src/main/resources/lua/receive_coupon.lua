if(redis.call('exists', KEYS[1]) == 0) then
    return 1
end
if(tonumber(redis.call('hget', KEYS[1], 'totalNum')) <= 0) then
    return 2
end
if(tonumber(redis.call('time')[1]) > tonumber(redis.call('hget', KEYS[1], 'issueEndTime'))) then
    return 3
end
if(tonumber(redis.call('hget', KEYS[1], 'userLimit')) < redis.call('hincrby', KEYS[2], ARGV[1], 1)) then
    return 4
end
redis.call('hincrby', KEYS[1], "totalNum", "-1")
return 0