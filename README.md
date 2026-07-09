# Resume-Job Match Score

Final project for the Building AI course

## Summary

A simple NLP tool that scores how well a resume matches a job description using TF-IDF and cosine similarity, helping job seekers quickly see how well-aligned their resume is before applying. Building AI course project.

## Background

Job seekers often apply to dozens of roles without a clear, objective sense of how well their resume actually matches each job description. Tailoring a resume for every application takes time, and it's easy to miss important keyword overlaps or overestimate fit based on gut feeling alone.

* Manually comparing a resume against a job description is time-consuming, especially when applying to many roles
* It's hard to know objectively which keywords or skills are missing from a resume
* Job seekers may waste time applying to poorly matched roles, or undersell themselves for well-matched ones

My personal motivation: I've been actively job hunting for Senior Software Engineer roles, and have spent considerable time manually comparing my resume against job descriptions to tailor my applications. This project turns that manual process into something quick, repeatable, and quantifiable.

## How is it used?

The tool takes two pieces of text as input — a resume and a job description — and outputs a percentage match score.

It's intended to be used by:
* **Job seekers**, before submitting an application, to quickly gauge fit and identify if their resume needs tailoring
* **Recruiters**, as a lightweight first-pass screening aid (with the understanding that it should never fully replace human judgement)

In practice, a user would paste in their resume text and a target job description, run the script, and get an instant similarity score as a percentage.

```python
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
```

## Data sources and AI methods

This project doesn't depend on any external dataset — it works on whatever resume text and job description text the user provides as input, making it fully self-contained and privacy-friendly (no data ever leaves the user's machine).

**AI technique used: TF-IDF (Term Frequency-Inverse Document Frequency) + Cosine Similarity**

* **TF-IDF** converts the resume and job description into numeric vectors, giving more weight to words that are distinctive to a document and less weight to common words that appear everywhere
* **Cosine similarity** measures how closely aligned the two vectors are, producing a single score between 0 and 100%

This project is implemented in **two languages** to demonstrate the same underlying logic:

| Language | File |
| -------- | ---- |
| Python   | `python/match_score.py` |
| Java     | `java/ResumeJobMatcher.java` |

Both implementations follow the identical algorithm: tokenize → compute term frequency → compute inverse document frequency → build TF-IDF vectors → calculate cosine similarity.

## Challenges

This project has a few important limitations:

* It only measures **keyword overlap**, not actual semantic meaning — a resume could use different words for the same skill (e.g. "led a team" vs "managed a team") and be scored lower than it should
* It can be **gamed by keyword stuffing**, where a resume packed with buzzwords scores artificially high without genuine relevant experience
* It does not understand **context or seniority** — mentioning "Java" once is treated the same as years of deep Java expertise
* It should never be used as a sole hiring decision tool — fairness, bias, and human judgement must always remain part of any real recruitment process

## What next?

* Add **synonym and skill-taxonomy matching** (e.g. recognising "Spring Boot" and "Spring Framework" as related)
* Extract and highlight **missing keywords** from the job description so users know exactly what to add
* Build a simple **web interface** so non-technical users can use it without running code
* Train a more advanced model using **word embeddings** (e.g. Word2Vec or sentence transformers) to capture semantic similarity instead of just keyword overlap
* Package the Java version as a **Spring Boot REST API** so it could be integrated into a real job-search or ATS tool

## Acknowledgments

* Built using concepts taught in the [Building AI](https://buildingai.elementsofai.com/) course by Reaktor and the University of Helsinki, particularly the TF-IDF and vector similarity exercises
* No external code, datasets, or assets from third parties were used in this project
