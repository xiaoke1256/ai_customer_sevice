
如何下载并在PC上安装千问大模型
===

安装 modelscope 和 transformers 库
```shell
pip install modelscope transformers
```

下载模型
```shell
export MODELSCOPE_CACHE=E:\ai_model # 可选：指定下载路径
# 建议下载以下模型，
#Qwen3-0.6B
#Qwen2.5-1.5B
#Qwen2-7B（需量化后运行）
modelscope download --model Qwen/Qwen3-0.6B
```
先下载并安装 conda ， 然后安装 vLLM。

以下命令要以管理员模式在PowerShell中运行

如果install的过程遇到“文件或目录不存在”异常，可能是因为系统不支持长文件名，请在 regedit 中将 HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\FileSystem 中项 LongPathsEnabled 值设置为 1。
```shell
conda create -n vllm python=3.10 -y
conda activate vllm
pip install vLLM
```

启动服务
```shell
python -m vLLM.entrypoints.openai.api_server \
  --model /your/target/path/Qwen/Qwen3-14B \
  --trust-remote-code \
  --tensor-parallel-size 4 \ # 根据你的GPU数量调整，例如4张A100
  --max-model-len 8192 \
  --dtype half # 使用半精度以节省显存
```

测试服务
```shell
curl -X POST "http://localhost:8000/v1/chat/completions" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer EMPTY" \
  -d '{
    "model": "/your/target/path/Qwen/Qwen3-14B",
    "messages": [{"role": "system", "content": "你是一个有用的助手。"}, {"role": "user", "content": "你好！"}],
    "temperature": 0.7
  }'
```