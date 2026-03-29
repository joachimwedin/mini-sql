import {describe, it, expect, beforeEach, vi} from 'vitest';

vi.mock('../wasm/mini-sql-engine.mjs', () => ({
    helloWorld: vi.fn(() => 'Hello, World!'),
}));

describe('Mini SQL API', () => {
    beforeEach(async () => {
        vi.resetModules();
        vi.clearAllMocks();
    });

    it('Given uninitialized API, When init() is called, Then it should be ready', async () => {
        // Reimport api after module reset
        const {default: api} = await import('../index');

        // When
        await api.init();

        // Then
        expect(api.isReady()).toBe(true);
    });

    it('Given already initialized API, When init() is called again, Then it should remain ready', async () => {
        // Reimport api after module reset
        const {default: api} = await import('../index');

        // When
        await api.init();
        const firstState = api.isReady();

        await api.init();
        const secondState = api.isReady();

        // Then
        expect(firstState).toBe(true);
        expect(secondState).toBe(true);
    });

    it('Given uninitialized API, When isReady() is called, Then it should return false', async () => {
        // Reimport api after module reset
        const {default: api} = await import('../index');

        // Given
        // API module in uninitialized state (default state)

        // When
        const ready = api.isReady()

        // Then
        expect(ready).toBe(false);
    });

    it('Given initialized API, When isReady() is called, Then it should return true', async () => {
        // Reimport api after module reset
        const {default: api} = await import('../index');

        // Given
        // API module in uninitialized state

        // When
        await api.init();

        // Then
        expect(api.isReady()).toBe(true);
    });

    it('Given initialized API, When helloWorld() is called, Then it should return the mocked message', async () => {
        // Reimport api after module reset
        const {default: api} = await import('../index');

        // Given
        // API module needs to be initialized
        await api.init();

        // When
        const result = api.helloWorld();

        // Then
        expect(result).toBe('Hello, World!');
    });

    it('Given uninitialized API, When helloWorld() is called, Then it should throw an error', async () => {
        // Reimport api after module reset
        const {default: api} = await import('../index');

        // Given
        // API module in uninitialized state (no init() call)

        // When & Then
        expect(() => {
            api.helloWorld();
        }).toThrow('Engine not initialized. Call init() first.');
    });

    it('Given imported API module, When checking function types, Then all required functions should exist', async () => {
        // Reimport api after module reset
        const api = await import('../index');

        // Given
        // API module has been imported

        // Then
        expect(typeof api.init).toBe('function');
        expect(typeof api.isReady).toBe('function');
        expect(typeof api.helloWorld).toBe('function');
    });

    it('Given imported API module, When accessing default export, Then it should contain all functions', async () => {
        // Reimport api after module reset
        const {default: apiDefault} = await import('../index');

        // Given
        const defaultExport = apiDefault;

        // Then
        expect(defaultExport).toHaveProperty('init');
        expect(defaultExport).toHaveProperty('isReady');
        expect(defaultExport).toHaveProperty('helloWorld');
    });
});

