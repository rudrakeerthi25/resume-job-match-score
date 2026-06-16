"""
Resume-to-Job Match Score
A simple NLP tool that scores how well a resume matches a job description
using TF-IDF vectorization and cosine similarity.

Building AI course project demo
"""

import math
import re


def tokenize(text):
    """Lowercase and split text into words, removing punctuation."""
    text = text.lower()
    words = re.findall(r'\b[a-z]+\b', text)
    return words


def compute_tf(words):
    """Term frequency: count of each word divided by total word count."""
    tf = {}
    total = len(words)
    for word in words:
        tf[word] = tf.get(word, 0) + 1
    for word in tf:
        tf[word] = tf[word] / total
    return tf


def compute_idf(documents):
    """Inverse document frequency across all documents (resume + JD)."""
    N = len(documents)
    idf = {}
    vocabulary = set(word for doc in documents for word in doc)

    for word in vocabulary:
        doc_count = sum(1 for doc in documents if word in doc)
        idf[word] = math.log((N + 1) / (doc_count + 1)) + 1  # smoothed idf

    return idf, vocabulary


def compute_tfidf_vector(tf, idf, vocabulary):
    """Build a tf-idf vector for one document, ordered by vocabulary list."""
    return [tf.get(word, 0) * idf[word] for word in vocabulary]


def cosine_similarity(vec1, vec2):
    """Cosine similarity between two vectors: dot product / (norm1 * norm2)."""
    dot_product = sum(a * b for a, b in zip(vec1, vec2))
    norm1 = math.sqrt(sum(a * a for a in vec1))
    norm2 = math.sqrt(sum(b * b for b in vec2))

    if norm1 == 0 or norm2 == 0:
        return 0.0

    return dot_product / (norm1 * norm2)


def match_score(resume_text, job_description_text):
    """
    Calculates a match score (0-100%) between a resume and a job description
    using TF-IDF vectors and cosine similarity.
    """
    resume_words = tokenize(resume_text)
    jd_words = tokenize(job_description_text)

    documents = [resume_words, jd_words]

    tf_resume = compute_tf(resume_words)
    tf_jd = compute_tf(jd_words)

    idf, vocabulary = compute_idf(documents)
    vocabulary = list(vocabulary)

    vec_resume = compute_tfidf_vector(tf_resume, idf, vocabulary)
    vec_jd = compute_tfidf_vector(tf_jd, idf, vocabulary)

    similarity = cosine_similarity(vec_resume, vec_jd)
    return round(similarity * 100, 2)


def main():
    resume_text = """
    Senior Software Engineer with 7+ years of experience in Java Spring Boot
    microservices and REST API development. Led API development at Westpac
    Banking Group. Experience with Kafka, SQL, Docker, Jenkins CICD and
    Agile Scrum methodology. AWS Cloud Practitioner certified.
    """

    job_description_text = """
    We are seeking a Senior Software Developer with experience in Java
    Spring Boot and microservices. Hands-on experience with REST API design,
    SQL databases, Docker containerisation and CICD pipelines using Jenkins.
    Experience in Agile Scrum methodology required. Banking domain experience
    is a strong advantage.
    """

    score = match_score(resume_text, job_description_text)
    print(f"Resume-Job Match Score: {score}%")


if __name__ == "__main__":
    main()
