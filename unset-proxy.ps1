# 取消Git代理
git config --global --unset http.proxy
git config --global --unset https.proxy

Write-Host "Git代理已取消" -ForegroundColor Green