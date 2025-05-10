# Git代理设置脚本
param (
    [string]$Mode = "help",  # 模式：set, unset, status, help
    [string]$Host = "127.0.0.1",  # 代理主机
    [string]$Port = "7890"  # 代理端口
)

# 显示帮助信息
function Show-Help {
    Write-Host "Git代理设置工具" -ForegroundColor Cyan
    Write-Host "用法: .\git-proxy.ps1 [模式] [主机] [端口]" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "模式:" -ForegroundColor Yellow
    Write-Host "  set    - 设置Git代理 (默认主机:127.0.0.1, 端口:7890)"
    Write-Host "  unset  - 取消Git代理"
    Write-Host "  status - 显示当前Git代理设置"
    Write-Host "  help   - 显示帮助信息"
    Write-Host ""
    Write-Host "示例:" -ForegroundColor Yellow
    Write-Host "  .\git-proxy.ps1 set                  # 使用默认设置"
    Write-Host "  .\git-proxy.ps1 set 127.0.0.1 10809  # 指定主机和端口"
    Write-Host "  .\git-proxy.ps1 unset                # 取消代理"
    Write-Host "  .\git-proxy.ps1 status               # 查看当前设置"
}

# 设置Git代理
function Set-GitProxy {
    param (
        [string]$ProxyHost,
        [string]$ProxyPort
    )
    
    $proxyUrl = "http://${ProxyHost}:${ProxyPort}"
    
    git config --global http.proxy $proxyUrl
    git config --global https.proxy $proxyUrl
    
    Write-Host "Git代理已设置为: $proxyUrl" -ForegroundColor Green
}

# 取消Git代理
function Unset-GitProxy {
    git config --global --unset http.proxy
    git config --global --unset https.proxy
    
    Write-Host "Git代理已取消" -ForegroundColor Green
}

# 显示Git代理状态
function Show-GitProxyStatus {
    $httpProxy = git config --global --get http.proxy
    $httpsProxy = git config --global --get https.proxy
    
    Write-Host "当前Git代理设置:" -ForegroundColor Cyan
    
    if ($httpProxy) {
        Write-Host "HTTP代理:  $httpProxy" -ForegroundColor Yellow
    } else {
        Write-Host "HTTP代理:  未设置" -ForegroundColor Gray
    }
    
    if ($httpsProxy) {
        Write-Host "HTTPS代理: $httpsProxy" -ForegroundColor Yellow
    } else {
        Write-Host "HTTPS代理: 未设置" -ForegroundColor Gray
    }
}

# 主函数
switch ($Mode.ToLower()) {
    "set" {
        Set-GitProxy -ProxyHost $Host -ProxyPort $Port
    }
    "unset" {
        Unset-GitProxy
    }
    "status" {
        Show-GitProxyStatus
    }
    default {
        Show-Help
    }
} 