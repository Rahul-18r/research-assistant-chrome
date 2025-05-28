document.addEventListener('DOMContentLoaded', () => {
    chrome.storage.local.get(['researchNotes'], function(result) {
        if (result.researchNotes) {
            document.getElementById('notes').value = result.researchNotes;
        }
    });

    document.querySelector('.Submit').addEventListener('click', handleOperationSubmit);
    document.getElementById('saveNotesBtn').addEventListener('click', saveNotes);
});

async function handleOperationSubmit() {
    try {
        const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
        const [{ result }] = await chrome.scripting.executeScript({
            target: { tabId: tab.id },
            function: () => window.getSelection().toString()
        });

        if (!result) {
            showResult('Please select some text first.');
            return;
        }

        const selectedOperation = document.getElementById('operationDropdown').value;

        const response = await fetch('http://localhost:8080/api/research/process', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                content: result,
                operation: selectedOperation
            })
        });

        if (!response.ok) {
            throw new Error(`API Error: ${response.status}`);
        }

        const buffer = await response.arrayBuffer();
        const decodedText = decodeUTF8(buffer);
        const cleanedText = cleanResponseText(decodedText);
        showResult(cleanedText);
    } catch (error) {
        showResult('Error: ' + error.message);
    }
}

function decodeUTF8(buffer) {
    const decoder = new TextDecoder('utf-8');
    return decoder.decode(buffer);
}

async function saveNotes() {
    const notes = document.getElementById('notes').value;
    chrome.storage.local.set({ 'researchNotes': notes }, function() {
        alert('Notes saved successfully');
    });
}
function showResult(content) {
    const resultBox = document.getElementById('result');
    if (resultBox) {
        const plainText = content.replace(/<br\s*\/?>/gi, '\n').replace(/<[^>]+>/g, '');
        resultBox.value = plainText;
        resultBox.style.height = 'auto'; // Reset height
        resultBox.style.height = Math.min(resultBox.scrollHeight + 2, 400) + 'px';
    }
}
function cleanResponseText(text) {
    return text
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/â€¢/g, '•')
        .replace(/[•*-]\s+(?=[^\n])/g, '• ')
        .replace(/\*/g, '')
        .replace(/\n{2,}/g, '\n')
        .replace(/\n/g, '<br>')
        .replace(/â€™/g, '’')
        .replace(/â€œ/g, '“')
        .replace(/â€/g, '”')
        .trim();
}
