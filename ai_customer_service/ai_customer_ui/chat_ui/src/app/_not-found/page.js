'use client';

export default function NotFound() {
  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>404 - 页面未找到</h1>
      <p>抱歉,您要查找的页面不存在。</p>
      <a href="/">返回首页</a>
    </div>
  );
}