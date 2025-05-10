# 设置Git代理
$proxyHost = "127.0.0.1"
$proxyPort = "7890"
$proxyUrl = "http://${proxyHost}:${proxyPort}"

git config --global http.proxy $proxyUrl
git config --global https.proxy $proxyUrl

Write-Host "Git代理已设置为: $proxyUrl" -ForegroundColor Green 