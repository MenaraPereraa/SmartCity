module.exports = {
  content: [
    './src/main/webapp/**/*.jsp',
    './src/main/webapp/**/*.html',
    './src/main/webapp/**/*.js'
  ],
  theme: {
    extend: {
      colors: {
        mist: '#f4f7f9',
        ink: '#163b4d',
        brand: '#0f5d7a',
        'brand-dark': '#0c445c',
        signal: '#f0a81c',
        line: '#dfe7ec'
      },
      fontFamily: {
        sans: ['"Public Sans"', 'ui-sans-serif', 'system-ui', 'sans-serif']
      }
    }
  },
  plugins: []
};