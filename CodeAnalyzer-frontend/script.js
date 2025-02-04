document.getElementById('codeForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const analyzeBtn = document.getElementById('analyzeBtn');
  const codeInput = document.getElementById('codeInput');
  const errorMessage = document.getElementById('errorMessage');
  const resultCard = document.getElementById('resultCard');
  const resultText = document.getElementById('resultText');

  // Reset UI
  errorMessage.classList.add('d-none');
  resultCard.classList.add('d-none');
  resultText.innerHTML = '';

  if (!codeInput.value.trim()) {
    errorMessage.textContent = 'Please enter some code.';
    errorMessage.classList.remove('d-none');
    return;
  }

  try {
    analyzeBtn.disabled = true;
    analyzeBtn.innerHTML = '<div class="spinner-border spinner-border-sm" role="status"></div> Analyzing...';

    const response = await fetch('http://localhost:8080/ai/analyze-code', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ codeContent: codeInput.value }),
    });

    if (!response.ok) throw new Error('Failed to analyze code');
    
    const data = await response.text();
    const cleanedData = data.replace(/\*\*/g, '').replace(/\*/g, '');
    
    // Add colored sections and animate typing
    const formattedData = cleanedData
      .replace(/✤ Bugs/g, '✤ Bugs')
      .replace(/✤ Optimizations/g, '✤ Optimizations')
      .replace(/✤ Best Practices/g, '✤ Best Practices')
      .replace(/✤ Security Issues/g, '✤ Security Issues');

    resultCard.classList.remove('d-none');
    await typeWriterEffect(resultText, formattedData);
    
  } catch (error) {
    errorMessage.textContent = error.message;
    errorMessage.classList.remove('d-none');
  } finally {
    analyzeBtn.disabled = false;
    analyzeBtn.textContent = 'Analyze Code';
  }
});

async function typeWriterEffect(element, text, speed = 5) {
  let index = 0;
  element.innerHTML = '';
  
  return new Promise(resolve => {
    function type() {
      if (index < text.length) {
        element.innerHTML += text.charAt(index);
        index++;
        setTimeout(type, speed);
        
        // Auto-scroll to bottom
        element.parentElement.scrollTop = element.parentElement.scrollHeight;
      } else {
        resolve();
      }
    }
    type();
  });
}