let wasmModule: any = null;

export async function init(): Promise<void> {
  if (wasmModule) return;
  
  try {
    wasmModule = await import('./generated/wasm/mini-sql-engine.mjs');
  } catch (error) {
    console.error('Failed to load WASM engine:', error);
    throw new Error('Could not initialize SQL engine');
  }
}

export function isReady(): boolean {
  return wasmModule !== null;
}

export function helloWorld(): string {
  if (!wasmModule) {
    throw new Error('Engine not initialized. Call init() first.');
  }
  return wasmModule.helloWorld();
}

export default { init, isReady, helloWorld };