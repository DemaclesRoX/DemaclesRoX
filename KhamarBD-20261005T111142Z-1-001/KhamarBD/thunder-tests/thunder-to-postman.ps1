# =============================================================================
#  thunder-to-postman.ps1
#  Thunder Client collection  ->  Postman Collection v2.1
# =============================================================================
#  Keno?  Thunder Client er "Import" feature ta Pro (paid). Postman er free
#  version e import free. Tai ei script ta diye Thunder collection ke Postman
#  format e convert kora hoy.
#
#  CHALATE:
#     cd e:\Downloads\KhamarBD
#     powershell -ExecutionPolicy Bypass -File .\thunder-tests\thunder-to-postman.ps1
#
#  INPUT  : thunder-tests\KhamarBD_Collection.json
#  OUTPUT : postman\KhamarBD.postman_collection.json
#
#  NOTE: Thunder collection edit korle abar ei script chalan - Postman file
#        notun kore toiri hoye jabe.
# =============================================================================

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$src  = Join-Path $root 'thunder-tests\KhamarBD_Collection.json'
$dst  = Join-Path $root 'postman\KhamarBD.postman_collection.json'

if (-not (Test-Path $src)) { throw "Input pai ni: $src" }

$thunder = Get-Content $src -Raw | ConvertFrom-Json

# --- folder id -> request list -----------------------------------------------
$byFolder = @{}
foreach ($f in $thunder.folders) { $byFolder[$f._id] = New-Object System.Collections.ArrayList }

foreach ($r in $thunder.requests) {
    $req = [ordered]@{
        method = $r.method
        header = @()
        url    = $r.url          # Postman v2.1 plain string url ke support kore
    }

    if ($r.body -and $r.body.raw) {
        $req.header = @( @{ key = 'Content-Type'; value = 'application/json' } )
        $req.body   = [ordered]@{
            mode    = 'raw'
            raw     = $r.body.raw
            options = @{ raw = @{ language = 'json' } }
        }
    }

    $item = [ordered]@{ name = $r.name; request = $req }

    if (-not $byFolder.ContainsKey($r.containerId)) {
        $byFolder[$r.containerId] = New-Object System.Collections.ArrayList
    }
    $null = $byFolder[$r.containerId].Add($item)
}

# --- build groups in folder order --------------------------------------------
$items = New-Object System.Collections.ArrayList
foreach ($f in $thunder.folders) {
    $null = $items.Add([ordered]@{
        name = $f.name
        item = @($byFolder[$f._id])
    })
}

$collection = [ordered]@{
    info = [ordered]@{
        name        = 'KhamarBD API'
        _postman_id = [guid]::NewGuid().ToString()
        schema      = 'https://schema.getpostman.com/json/collection/v2.1.0/collection.json'
        description = 'KhamarBD backend API. Folder 1 theke numbered order e cholbe - id dependency ache (Farm banate user lage, Lot banate farm lage). Sob request body *RequestDto.java er sathe mile. NOTE: H2 in-memory, tai server restart korle data chole jay.'
    }
    item = @($items)
}

$dir = Split-Path -Parent $dst
if (-not (Test-Path $dir)) { $null = New-Item -ItemType Directory -Path $dir -Force }

$collection | ConvertTo-Json -Depth 15 | Set-Content -Path $dst -Encoding utf8

# --- verify -------------------------------------------------------------------
$check = Get-Content $dst -Raw | ConvertFrom-Json
$reqCount = 0
foreach ($g in $check.item) { $reqCount += @($g.item).Count }

Write-Host ''
Write-Host ' Convert DONE' -ForegroundColor Green
Write-Host "   input  : $src"
Write-Host "   output : $dst"
Write-Host ("   folders  = {0}" -f @($check.item).Count)
Write-Host ("   requests = {0}" -f $reqCount)
Write-Host ''