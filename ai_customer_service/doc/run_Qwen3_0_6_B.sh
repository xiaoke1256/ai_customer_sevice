docker stop Qwen3-0.6B
docker rm   Qwen3-0.6B

#docker run -d \
#  # 仅映射 GPU-2 与 GPU-3（两张 A10 共 46 GB）用于 TP=2
#  #--gpus '"device=2,3"' \
#  # 容器名称与模型同名，方便管理
#  --name Qwen3-0.6B \
#  # 模型权重挂载（只读）
#  -v /root/ai_model/modelscope/hub/models/Qwen/Qwen3-0___6B:/models/Qwen3-0___6:ro \
#  # 宿主机端口映射
#  -p 11435:8000 \
#  # 共享内存充足，NCCL 需要
#  --ipc=host \
#  # 关闭 seccomp & apparmor，避免 pthread/clone 被拦截
#  --security-opt seccomp=unconfined \
#  --security-opt apparmor=unconfined \
#  # 进程数 & 文件描述符上限放大，防止多线程报错
#  --ulimit nproc=65535:65535 \
#  --ulimit nofile=65536:65536 \
#  # 限制 OpenBLAS/OpenMP/Numba 线程数，减少上下文切换
#  -e OPENBLAS_NUM_THREADS=1 \
#  -e OMP_NUM_THREADS=1 \
#  -e NUMBA_NUM_THREADS=1 \
#  # 绕过 NumPy 1.26+ 在容器里的 dispatcher 竞态 bug
#  -e NUMPY_DISABLE_CPU_FEATURES="sse3 ssse3 sse41 popcnt avx avx2 fma3" \
#  # 官方镜像 ≥ 0.11 已支持 Qwen3-VL
#  vllm/vllm-openai:latest \
#  # 以下均为 vLLM 启动参数
#  --model /models/Qwen3-0___6 \
#  # 两张卡做 Tensor Parallel
##  --tensor-parallel-size 2 \
#  # 显存利用率留一点余量给 NCCL 缓存
##  --gpu-memory-utilization 0.85 \
#  # Qwen3 原生支持 32 k，如需 64 k 再翻倍
#  --max-model-len 32768 \
#  # 模型含自定义视觉/音频 processor，必须加
#  --trust-remote-code \
#  # 与官方对齐，节省显存（32B 权重 ≈ 60 GB fp16）
#  --dtype bfloat16


docker run -d \
  --name Qwen3-0.6B \
  -v /root/ai_model/modelscope/hub/models/Qwen/Qwen3-0___6B:/models/Qwen3-0___6:ro \
  -p 11435:8000 \
  --ipc=host \
  --security-opt seccomp=unconfined \
  --security-opt apparmor=unconfined \
  --ulimit nproc=65535:65535 \
  --ulimit nofile=65536:65536 \
  -e OPENBLAS_NUM_THREADS=1 \
  -e OMP_NUM_THREADS=1 \
  -e NUMBA_NUM_THREADS=1 \
  -e NUMPY_DISABLE_CPU_FEATURES="sse3 ssse3 sse41 popcnt avx avx2 fma3" \
  vllm/vllm-openai:latest \
  --model /models/Qwen3-0___6 \
  --max-model-len 32768 \
  --trust-remote-code \
  --dtype bfloat16