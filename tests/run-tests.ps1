$ErrorActionPreference = 'Stop'
$project = Split-Path $PSScriptRoot -Parent
$out = Join-Path $project 'build/matcher-tests'
New-Item -ItemType Directory -Force $out | Out-Null
$src = Join-Path $project 'app/src/main/java/za/ac/pantry'
javac -d $out "$src/Ingredient.java" "$src/Recipe.java" "$src/Matcher.java" "$PSScriptRoot/MatcherTest.java"
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
java -cp $out MatcherTest
if ($LASTEXITCODE -ne 0) { throw 'Matching test failed' }
